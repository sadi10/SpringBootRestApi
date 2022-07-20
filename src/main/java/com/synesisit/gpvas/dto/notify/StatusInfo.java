package com.synesisit.gpvas.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusInfo {

    private String statusCode;
    private String referenceCode;
    private String serverReferenceCode;

}
