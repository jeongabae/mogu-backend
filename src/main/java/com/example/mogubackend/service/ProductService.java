package com.example.mogubackend.service;

import com.example.mogubackend.common.DealStatus;
import com.example.mogubackend.dto.product.request.ProductRegisterRequest;
import com.example.mogubackend.dto.product.request.ProductUpdateRequest;
import com.example.mogubackend.dto.product.response.ProductListBySellerResponse;
import com.example.mogubackend.entity.Favorite;
import com.example.mogubackend.entity.Member;
import com.example.mogubackend.entity.Product;
import com.example.mogubackend.entity.ProductImage;
import com.example.mogubackend.repository.FavoriteRepository;
import com.example.mogubackend.repository.MemberRepository;
import com.example.mogubackend.repository.ProductImageRepository;
import com.example.mogubackend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final MemberRepository memberRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    private final FavoriteService favoriteService;

    public ProductService(MemberRepository memberRepository, ProductImageRepository productImageRepository,
                          ProductRepository productRepository, FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
        this.memberRepository = memberRepository;
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
    }

    @Autowired
    private ProductImageService productImageService;

    @Transactional
    public Product registerProduct(UUID sellerId, ProductRegisterRequest request) {
        // sellerId로 Member를 찾음
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 해당하는 회원을 찾을 수 없습니다: " + sellerId));


//        // 회원의 상품 개수 확인
//        long productCount = memberRepository.countProductsBySellerId(sellerId);
//        if (productCount >= 3) {
//            throw new IllegalStateException("한 회원이 등록할 수 있는 상품은 최대 3개입니다.");
//        }
        // 회원의 모집 중인 상품 개수 확인
        long productCount = memberRepository.countProductsBySellerIdAndStatus(sellerId, DealStatus.모집중);
        if (productCount >= 3) {
            throw new IllegalStateException("한 회원이 등록할 수 있는 모집 중인 상품은 최대 3개입니다.");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setUrl(request.url());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setQty(request.qty()); //모집수량
        product.setRemainingQty(request.qty()); //잔여수량
        product.setEndDate(request.endDate());
        product.setMqq(request.mqq());
        product.setContent(request.content());
        product.setLocation(request.location());
        product.setLatitude(request.latitude());
        product.setLongitude(request.longitude());
        product.setLocationName(request.locationName());
        product.setChatUrl(request.chatUrl());
        product.setSeller(seller);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        MultipartFile file = request.image();
        ProductImage productImage = new ProductImage();
        productImage.setUploadFileName(file.getOriginalFilename());
        productImage.setStoreFileName(productImageService.saveImage(file));
        productImage.setProduct(product);
        productImageRepository.save(productImage);
        product.setImage(productImage);

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProductWithImages(Long productId) {
        // Product 엔터티 조회
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            // Product에 연결된 모든 ProductImage 엔터티 조회
            ProductImage productImage = product.getImage();
            productImageService.deleteImage(productImage.getStoreFileName());
            // 연결된 모든 ProductImage 엔터티 삭제
            productImageRepository.delete(productImage);

            List<Favorite> favorites = favoriteRepository.findByProduct(product);
//             연결된 모든 Favorite 엔터티 삭제
            favoriteRepository.deleteAll(favorites);

            // Product 엔터티 삭제
            productRepository.delete(product);
        } else {
            // 지정된 ID에 해당하는 Product가 없을 경우 처리할 내용 추가
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
    }

//    public Page<Product> getProducts(Pageable pageable) {
//        return productRepository.findAll(pageable);
//    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findByStatus(DealStatus.모집중, pageable);
    }

    @Transactional
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public String getChatUrlByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        return product.getChatUrl();
    }

    @Transactional
    public void updateProductStatus(Long productId, DealStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        product.setStatus(status);
        productRepository.save(product);
    }
    @Transactional
    public Page<Product> searchProductsByNameContainingIgnoreCase(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndStatus(keyword, DealStatus.모집중, pageable);
    }

    @Transactional
    public Page<Product> searchProductsByNameContainingOrderByRemainingQtyDesc(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndStatusOrderByRemainingQtyDesc(keyword, DealStatus.모집중, pageable);
    }

    @Transactional
    public Page<Product> searchProductsByNameContainingIgnoreCaseOrderByEndDateAsc(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndStatusOrderByEndDateAsc(keyword, DealStatus.모집중, pageable);
    }

    public boolean isProductBelongsToSeller(Long productId, UUID sellerId) {
        return productRepository.existsByProductIdAndSellerId(productId, sellerId);
    }

    @Transactional
    public void updateProductDetails(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        ProductImage existingImage = product.getImage();
        // 새로운 이미지 저장
        MultipartFile file = request.image();
        if (file != null && !file.isEmpty()) {
            productImageService.deleteImage(existingImage.getStoreFileName()); // 파일 시스템에서 이미지 삭제
            existingImage.setUploadFileName(file.getOriginalFilename());
            existingImage.setStoreFileName(productImageService.saveImage(file)); // 파일 시스템에 이미지 저장
        }

        // 나머지 상품 정보 업데이트
        product.setUrl(request.url());
        if (request.content() != null) {
            product.setContent(request.content());
        }
        if (request.chatUrl() != null) {
            product.setChatUrl(request.chatUrl());
        }

        productRepository.save(product); // Product 엔티티 저장
    }

    @Transactional
    public Page<Product> filterProducts(String category, String location, Pageable pageable) {
        if (category == null && location == null) {
            return productRepository.findAll(pageable);
        } else if (category != null && location == null) {
            return productRepository.findByCategory(category, pageable);
        } else if (category == null && location != null) {
            return productRepository.findByLocation(location, pageable);
        } else {
            return productRepository.findByCategoryAndLocation(category, location, pageable);
        }
    }

    @Transactional
    public boolean isProductFavorite(UUID memberId, Long productId) {
        // 여기서는 예시로 favoriteService를 사용하여 즐겨찾기 여부를 확인하는 메서드를 구현합니다.
        Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(memberId, productId);
        return favoriteId != null;
    }

    @Transactional
    public List<ProductListBySellerResponse> getOngoingProductsBySellerId(UUID sellerId) {
        List<Product> products = productRepository.findOngoingProductsBySellerIdOrderByCreatedAtDesc(sellerId);
        return products.stream().map(product -> new ProductListBySellerResponse(
                product.getId(),
                product.getName(),
                product.getStatus(),
                product.getRemainingQty(),
                product.getQty(),
                product.getMqq(),
                product.getLocation(),
                product.getImage().getStoreFileName()
        )).collect(Collectors.toList());
    }

    @Transactional
    public List<ProductListBySellerResponse> getCompletedProductsBySellerId(UUID sellerId) {
        List<Product> products = productRepository.findCompletedProductsBySellerIdOrderByEndDateDesc(sellerId);
        return products.stream().map(product -> new ProductListBySellerResponse(
                product.getId(),
                product.getName(),
                product.getStatus(),
                product.getRemainingQty(),
                product.getQty(),
                product.getMqq(),
                product.getLocation(),
                product.getImage().getStoreFileName()
        )).collect(Collectors.toList());
    }

//    @Transactional
//    public void updateProductStatus() {
//        LocalDate today = LocalDate.now();
//        List<Product> products = productRepository.findAllByStatusAndEndDateBefore(DealStatus.모집중, today.atStartOfDay());
//
//
//        for (Product product : products) {
//            product.setStatus(DealStatus.구매진행중);
//            productRepository.save(product);
//        }
//    }

    @Transactional
    public void updateProductStatus() {
        LocalDate today = LocalDate.now();
        List<Product> products = productRepository.findAllByStatusAndEndDateBefore(DealStatus.모집중, today.atStartOfDay());

        for (Product product : products) {
            int participationCount = product.getQty() - product.getRemainingQty();
            if (participationCount >= product.getMqq()) {
                product.setStatus(DealStatus.구매진행중);
            } else {
                product.setStatus(DealStatus.공구종료);
            }
            productRepository.save(product);
        }
    }
}
