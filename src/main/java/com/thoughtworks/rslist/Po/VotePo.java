package com.thoughtworks.rslist.Po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotePo {
    @Id
    @GeneratedValue
    private int id;
    private int voteNum;
    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPo rsEvent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPo user;


}
