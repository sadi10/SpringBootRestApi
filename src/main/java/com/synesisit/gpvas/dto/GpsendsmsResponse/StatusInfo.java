package com.synesisit.gpvas.dto.GpsendsmsResponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusInfo {

    @JsonProperty("totalAmountCharged")
    private String totalAmountCharged;

    @JsonProperty("statusCode")
    private String statusCode;

    @JsonProperty("referenceCode")
    private String referenceCode;

    @JsonProperty("serverReferenceCode")
    private String serverReferenceCode;

    @JsonProperty("errorInfo")
    private ErrorInfo errorInfo;






}
