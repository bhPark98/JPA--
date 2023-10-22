package com.fastcampus.ch4;

import javax.persistence.*;

@Entity
public class Cart {
    @Id
    @Column(name="cart_id")
    private long id;
    @OneToOne // FK 생김
    @JoinColumn(name="member_id", nullable = false) // inner join
    private Member member;

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
//                ", member=" + member +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
