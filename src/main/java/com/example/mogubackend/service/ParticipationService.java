package com.example.mogubackend.service;

import com.example.mogubackend.dto.participation.response.ParticipationResponse;
import com.example.mogubackend.dto.product.response.ProductListByBuyerResponse;
import com.example.mogubackend.entity.Participation;
import com.example.mogubackend.entity.Product;
import com.example.mogubackend.entity.Member;
import com.example.mogubackend.repository.ParticipationRepository;
import com.example.mogubackend.repository.ProductRepository;
import com.example.mogubackend.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ParticipationService(ParticipationRepository participationRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.participationRepository = participationRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Participation participateInProduct(Long productId, int quantity, UUID memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (product.getRemainingQty() < quantity) {
            throw new IllegalArgumentException("남은 수량이 부족합니다.");
        }

        // 상품의 개당 가격과 입력 받은 수량을 곱하여 참여 가격 계산
        int participatePrice = product.getPrice() * quantity;

        product.setRemainingQty(product.getRemainingQty() - quantity); //상품 남은 수량 반영
        productRepository.save(product);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Participation participation = new Participation(); //참여자 정보 저장
        participation.setProduct(product); //무슨 상품에
        participation.setQuantity(quantity); //몇개
        participation.setPrice(participatePrice);//얼마
        participation.setMember(member); //누가 참여하는지

        return participationRepository.save(participation);
    }

    public List<ParticipationResponse> getParticipationByProductId(Long productId) {
        return participationRepository.findParticipationByProductId(productId);
    }

//    public boolean isUserParticipating(UUID memberId, Long productId) {
//        Optional<Participation> participation = participationRepository.findByMemberIdAndProductId(memberId, productId);
//        return participation.isPresent();
//    }
    public String checkParticipationStatus(UUID memberId, Long productId) {
        Participation participation = participationRepository.findByMemberIdAndProductId(memberId, productId);

        if (participation == null) {
            return "N";
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime participationTime = participation.getParticipateDate();

     if (participationTime.isAfter(now.minusHours(1))) {
         return "Cancellable";
        } else {
            return "Y";
        }
    }


//    @Transactional(readOnly = true)
//    public List<ProductListByBuyerResponse> getOngoingProductsByMemberId(UUID memberId) {
//        List<Product> products = participationRepository.findOngoingProductsByMemberId(memberId);
//        return products.stream().map(product -> new ProductListByBuyerResponse(
//                product.getId(),
//                product.getName(),
//                product.getStatus(),
//                product.getRemainingQty(),
//                product.getQty(),
//                getParticipateQuantityForMember(product, memberId),
//                getParticipatePriceForMember(product, memberId),
//                product.getLocation(),
//                product.getImage().getStoreFileName()
//        )).collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<ProductListByBuyerResponse> getOngoingProductsByMemberId(UUID memberId) {
        List<Product> products = participationRepository.findOngoingProductsByMemberId(memberId);

        return products.stream()
                .filter(product -> !product.getSeller().getId().equals(memberId)) // 판매자를 제외
                .map(product -> new ProductListByBuyerResponse(
                        product.getId(),
                        product.getName(),
                        product.getStatus(),
                        product.getRemainingQty(),
                        product.getQty(),
                        getParticipateQuantityForMember(product, memberId),
                        getParticipatePriceForMember(product, memberId),
                        product.getLocation(),
                        product.getImage().getStoreFileName()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductListByBuyerResponse> getCompletedProductsByMemberId(UUID memberId) {
        List<Product> products = participationRepository.findCompletedProductsByMemberId(memberId);
        return products.stream()
                .filter(product -> !product.getSeller().getId().equals(memberId)) // 판매자를 제외
                .map(product -> new ProductListByBuyerResponse(
                product.getId(),
                product.getName(),
                product.getStatus(),
                product.getRemainingQty(),
                product.getQty(),
                getParticipateQuantityForMember(product, memberId),
                getParticipatePriceForMember(product, memberId),
                product.getLocation(),
                product.getImage().getStoreFileName()
        )).collect(Collectors.toList());
    }

    private int getParticipateQuantityForMember(Product product, UUID memberId) {
        return product.getParticipations().stream()
                .filter(participation -> participation.getMember().getId().equals(memberId))
                .mapToInt(Participation::getQuantity)
                .sum();
    }

    private int getParticipatePriceForMember(Product product, UUID memberId) {
        return product.getParticipations().stream()
                .filter(participation -> participation.getMember().getId().equals(memberId))
                .mapToInt(Participation::getPrice)
                .findFirst()
                .orElse(0);
    }

    public boolean deleteParticipation(UUID memberId, Long productId) {
        Participation participation = participationRepository.findByMemberIdAndProductId(memberId, productId);

        if (participation != null) {
            participationRepository.delete(participation);
            return true;
        } else {
            return false;
        }
    }
}