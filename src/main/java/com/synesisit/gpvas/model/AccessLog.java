package com.synesisit.gpvas.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbl_access_log")
@ToString
public class AccessLog implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id", length = 25)
    private String transactionId;

    @Column(name = "server_reference_code", length = 25)
    private String serverReferenceCode;

    @Column(name = "request_body", columnDefinition="Text")
    private String requestBody;


    @Column(name = "response_body", columnDefinition="Text")
    private String responseBody;

    @Column(name = "response_from")
    private String responseFrom;

    @Column(name = "access_channel", length = 10)
    private String accessChannel;

    @Column(name = "msisdn", length = 60)
    private String msisdn;

    @Column(name = "service_identifier", length = 60)
    private String serviceIdentifier;

    @Column(name="message", columnDefinition="Text")
    private String message;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "request_from")
    private String requestFrom;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "subscription_id", length = 25)
    private String subscriptionId;

    @Column(name = "is_processed", length = 1)
    private boolean isProcessed;

    @Column(name = "insert_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date insertDate;

    @Column(name = "updated_date", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedDate;

}
