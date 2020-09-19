package com.thoughtworks.rslist.Po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPo {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name")
    private String userName;
    private int age;
    private String gender;
    private String email;
    private String phone;
    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPo")
    private List<RsEventPo> rsEventPos;

}
