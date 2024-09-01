package com.example.mogubackend.controller;


import com.example.mogubackend.common.DealStatus;
import com.example.mogubackend.dto.product.request.ProductDetailsUpdateRequest;
import com.example.mogubackend.dto.product.request.ProductRegisterRequest;
import com.example.mogubackend.dto.product.request.ProductUpdateRequest;
import com.example.mogubackend.dto.product.response.ProductDetailResponse;
import com.example.mogubackend.dto.product.response.ProductListBySellerResponse;
import com.example.mogubackend.dto.product.response.ProductListResponse;
import com.example.mogubackend.entity.Member;
import com.example.mogubackend.entity.Product;
import com.example.mogubackend.security.UserAuthorize;
import com.example.mogubackend.service.FavoriteService;
import com.example.mogubackend.service.MemberService;
import com.example.mogubackend.service.ParticipationService;
import com.example.mogubackend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "공구 관련")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final MemberService memberService;
    private final ParticipationService participationService;
    private final FavoriteService favoriteService;

    @Autowired
    public ProductController(ProductService productService, MemberService memberService, ParticipationService participationService, FavoriteService favoriteService) {
        this.productService = productService;
        this.memberService = memberService;
        this.participationService = participationService;
        this.favoriteService = favoriteService;
    }


    @Operation(summary = "공구 등록 : 로그인 유저 토큰 필요")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @UserAuthorize
    public ResponseEntity<Product> registerProduct(@AuthenticationPrincipal User user, @ModelAttribute ProductRegisterRequest request) {
        Product product = productService.registerProduct(UUID.fromString(user.getUsername()), request);
        UUID memberId = UUID.fromString(user.getUsername());
        participationService.participateInProduct(product.getId(), request.participate_qty(), memberId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

//    @Operation(summary = "공구 조회(페이지 당 10개로 설정)")
//    @GetMapping
//    public ResponseEntity<Page<Product>> getProducts(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
////        Pageable pageable = PageRequest.of(page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<Product> products = productService.getProducts(pageable);
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }

    @Operation(summary = "공구 목록 조회(페이지 당 10개로 설정) : 로그인 유저 토큰 필요")
    @GetMapping
    public ResponseEntity<Page<ProductListResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productsPage = productService.getProducts(pageable);

        // Product -> ProductResponse 변환
        Page<ProductListResponse> productResponsePage = productsPage.map(product -> {
            boolean isFavorite = false;
            if (user != null) {
                Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(UUID.fromString(user.getUsername()), product.getId());
                isFavorite = favoriteId != null;
            }
            return new ProductListResponse(product, isFavorite);
        });

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @Operation(summary = "공구 목록 필터링 조회 : 로그인 유저 토큰 필요")
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductListResponse>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> filteredProducts = productService.filterProducts(category, location, pageable);

        Page<ProductListResponse> productResponsePage = filteredProducts.map(product -> {
            boolean isFavorite = false;
            if (user != null) {
                Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(UUID.fromString(user.getUsername()), product.getId());
                isFavorite = favoriteId != null;
            }
            return new ProductListResponse(product, isFavorite);
        });

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @Operation(summary = "공구 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        return productOptional.map(product -> {
            ProductDetailResponse response = new ProductDetailResponse(
                    product.getImage().getStoreFileName(),
                    product.getStatus(),
                    product.getCategory(),
                    product.getUrl(),
                    product.getName(),
                    product.getPrice(),
                    product.getRemainingQty(),
                    product.getEndDate(),
                    product.getContent(),
                    product.getLocation(),
                    product.getLatitude(),
                    product.getLongitude(),
                    product.getLocationName(),
                    product.getChatUrl()
            );
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "공구 오픈채팅방 링크 가져오기")
    @GetMapping("/{productId}/chatUrl")
    public ResponseEntity<String> getChatUrlByProductId(@PathVariable Long productId) {
        String chatUrl = productService.getChatUrlByProductId(productId);
        if (chatUrl != null) {
            return ResponseEntity.ok(chatUrl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "공구 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductWithImages(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공구 상태 변경")
    @PutMapping("/{productId}/status/{status}")
    public ResponseEntity<String> updateProductStatus(
            @PathVariable Long productId,
            @PathVariable DealStatus status) {
        productService.updateProductStatus(productId, status);
        return ResponseEntity.ok("공구 상태 변경 완료");
    }

    @Operation(summary = "최신순 검색(기본 검색) : 로그인 유저 토큰 필요")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductListResponse>> searchProducts(
            @Parameter(description = "검색 키워드") @RequestParam String keyword,
            @Parameter(description = "페이지 번호, 기본값: 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기, 기본값: 10") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productsPage = productService.searchProductsByNameContainingIgnoreCase(keyword, pageable);

        // Product -> ProductResponse 변환
        Page<ProductListResponse> productResponsePage = productsPage.map(product -> {
            boolean isFavorite = false;
            if (user != null) {
                Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(UUID.fromString(user.getUsername()), product.getId());
                isFavorite = favoriteId != null;
            }
            return new ProductListResponse(product, isFavorite);
        });
        return  new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @Operation(summary = "공구 검색(추천 순 : 잔여 수량 많은 순) : 로그인 유저 토큰 필요")
    @GetMapping("/search/recommend")
    public ResponseEntity<Page<ProductListResponse>> searchProductsByNameContainingOrderByRemainingQtyDesc(
            @Parameter(description = "검색 키워드") @RequestParam String keyword,
            @Parameter(description = "페이지 번호, 기본값: 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기, 기본값: 10") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productsPage = productService.searchProductsByNameContainingOrderByRemainingQtyDesc(keyword, pageable);

        // Product -> ProductResponse 변환
        Page<ProductListResponse> productResponsePage = productsPage.map(product -> {
            boolean isFavorite = false;
            if (user != null) {
                Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(UUID.fromString(user.getUsername()), product.getId());
                isFavorite = favoriteId != null;
            }
            return new ProductListResponse(product, isFavorite);
        });
        return  new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @Operation(summary = "공구 검색(추천 순 : 마감 임박일 가까운 순) : 로그인 유저 토큰 필요")
    @GetMapping("/search/end-date")
    public ResponseEntity<Page<ProductListResponse>> searchProductsByNameContainingIgnoreCaseOrderByEndDateAsc(
            @Parameter(description = "검색 키워드") @RequestParam String keyword,
            @Parameter(description = "페이지 번호, 기본값: 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기, 기본값: 10") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productsPage = productService.searchProductsByNameContainingIgnoreCaseOrderByEndDateAsc(keyword, pageable);

        // Product -> ProductResponse 변환
        Page<ProductListResponse> productResponsePage = productsPage.map(product -> {
            boolean isFavorite = false;
            if (user != null) {
                Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(UUID.fromString(user.getUsername()), product.getId());
                isFavorite = favoriteId != null;
            }
            return new ProductListResponse(product, isFavorite);
        });
        return  new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @Operation(summary = "현재 로그인된 유저 UUID로 해당 공구를 연 유저인지 조회")
    @GetMapping("/{productId}/checkSeller")
    public ResponseEntity<?> checkProductSeller(
            @PathVariable Long productId,
            @RequestParam UUID sellerId) {

        boolean belongsToSeller = productService.isProductBelongsToSeller(productId, sellerId);

        if (belongsToSeller) {
            return ResponseEntity.ok("Y");
        } else {
            return ResponseEntity.ok("N");
        }
    }

    @Operation(summary = "공구 수정(공지사항, 카카오 오픈채팅방 링크)")
    @PutMapping(value="/{productId}/updateDetails",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProductDetails(
            @PathVariable Long productId,
            @ModelAttribute ProductUpdateRequest request) {

        productService.updateProductDetails(productId, request);

        return ResponseEntity.ok("정상적으로 업데이트 되었습니다.");
    }

    @Operation(summary = "내가 진행중인 공구 목록 가져오기: 로그인 유저 토큰 필요")
    @GetMapping("/seller/ongoing")
    public ResponseEntity<List<ProductListBySellerResponse>> getOngoingProductsBySellerId(@AuthenticationPrincipal User user) {
        UUID sellerId = UUID.fromString(user.getUsername());
        List<ProductListBySellerResponse> products = productService.getOngoingProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "내가 진행한 공구 목록 가져오기: 로그인 유저 토큰 필요")
    @GetMapping("/seller/completed")
    public ResponseEntity<List<ProductListBySellerResponse>> getCompletedProductsBySellerId(@AuthenticationPrincipal User user) {
        UUID sellerId = UUID.fromString(user.getUsername());
        List<ProductListBySellerResponse> products = productService.getCompletedProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }
}

