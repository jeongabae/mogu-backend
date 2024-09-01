package com.example.mogubackend.entity;

import com.example.mogubackend.common.DealStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //상품명

    @Column
    private String url; //구매 링크

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductImage image; //상품이미지

    @Column(nullable = false)
    private String category; //카테고리(분류)

    @Column(nullable = false)
    private int price; //개당 가격

    @Column
    private int qty; //모집 수량

    @Column
    private int remainingQty; // 잔여 수량

    @Column
    private LocalDateTime endDate; //공구 마감일

    @Column(nullable = false)
    private int mqq; //최소 수량

    @Column(nullable = false)
    private String content; //공지

    @Column(nullable = false)
    private String location; //수령 장소 카테고리

    @Column(nullable = true)
    private double latitude; //위도

    @Column(nullable = true)
    private double longitude; //경도

    @Column(nullable = true)
    private String locationName; //수령 장소 설명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealStatus status = DealStatus.모집중;

    @Column
    private String chatUrl; //오픈채팅방링크

    @CreationTimestamp
    private LocalDateTime createdAt; //글 등록 시간(공구 시작일)

    @UpdateTimestamp
    private LocalDateTime updatedAt; //글 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Member seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Participation> participations;

}
