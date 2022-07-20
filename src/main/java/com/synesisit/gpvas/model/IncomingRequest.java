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
@Table(name = "incoming_request")
@ToString
public class IncomingRequest {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "msisdn", length = 60)
    private String msisdn;

    @Column(name = "server_reference_code", length = 50)
    private String serverReferenceCode;

    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "message")
    private String message;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "subscription_id", length = 50)
    private String subscriptionId;

    @Column(name = "registration_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "next_renewal_date" , length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextRenewalDate;

    @Column(name = "activation_date" , length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date activationDate;

    @Column(name = "subscription_status" , length = 50)
    private String subscriptionStatus;

    @Column(name = "status_changed_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusChangedDate;

    @Column(name = "product_identifier" , length = 10)
    private String productIdentifier;

    @Column(name = "is_sent", length = 1)
    private Integer isSent = 0;

    @Column(name = "is_processed", length = 1)
    private Integer isProcessed = 0;

    @Column(name = "insert_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date insertDate;

    @Column(name = "updated_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedDate;


}
