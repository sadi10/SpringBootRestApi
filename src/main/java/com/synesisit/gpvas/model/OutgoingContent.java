package com.synesisit.gpvas.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "outgoing_content")
@ToString
public class OutgoingContent {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference_code", length = 25)
    private String referenceCode;

    @Column(name = "transaction_id", length = 25)
    private String transactionId;

    @Column(name = "server_reference_code", length = 25)
    private String serverReferenceCode;

    @Column(name = "msisdn", length = 60)
    private String msisdn;

    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "content")
    private String content;

    @Column(name = "received_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;

    @Column(name = "send_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @Column(name = "delivery_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "status_code", length = 50)
    private String statusCode;

    @Column(name = "status_description", length = 500)
    private String statusDescription;

    @Column(name = "service_key", length = 60)
    private String serviceKey;

    @Column(name = "access_channel", length = 10)
    private String accessChannel;

    @Column(name = "sender_id", length = 10)
    private String senderId;

    @Column(name = "is_success", length = 1)
    private Integer isSuccess = 0;

    @Column(name = "is_sent", length = 1)
    private Integer isSent = 0;

    @Column(name = "insert_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date insertDate;

    @Column(name = "updated_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedDate;

}
