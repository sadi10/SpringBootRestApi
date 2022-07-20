package com.synesisit.gpvas.helper;

import com.synesisit.gpvas.dto.notify.StatusInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Component
public class LoggerHelper {
    @Value("${logfile.path}")
      // @Value("logfile.path")
       private String logFIlePath;

    public void fileService(String response, HttpServletRequest request, StatusInfo statusInfo) throws IOException {
          log.info("path:" +  logFIlePath);
           log.debug("logFIlePath: " + logFIlePath);

           String logfilename = "gp-vas-apiResponseLog.txt";
           String fileName= logFIlePath + logfilename;
           log.debug("fileName: " + fileName);
           File f = new File(fileName);
           f.createNewFile();
           FileWriter fw = new FileWriter(fileName, true);
           Timestamp ts = Timestamp.from(Instant.now());
           fw.write(System.lineSeparator());
           fw.write("Time::");
           fw.write(String.valueOf(ts));
           fw.write(System.lineSeparator());
           fw.write("From Api::");
           fw.write(request.getRemoteAddr());
           fw.write(System.lineSeparator());
           fw.write("Host Api::");
           fw.write(request.getRequestURI());
           fw.write(System.lineSeparator());
           fw.write("Request::");
           fw.write(response);
           fw.write(System.lineSeparator());
           fw.write("Response::");
           fw.write(System.lineSeparator());
           fw.write(String.valueOf(statusInfo));
           fw.write(System.lineSeparator());
           fw.write("======================================================================");
           fw.close();
    }


       public String padLeftZeros(String inputString, int length) {
              if (inputString.length() >= length) {
                     return inputString;
              }
              StringBuilder sb = new StringBuilder();
              while (sb.length() < length - inputString.length()) {
                     sb.append('0');
              }
              sb.append(inputString);

              return sb.toString();
       }



}
