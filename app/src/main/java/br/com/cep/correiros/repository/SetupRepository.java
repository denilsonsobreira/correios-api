package br.com.cep.correiros.repository;

import br.com.cep.correiros.model.Address;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SetupRepository {

    @Value("${correios.base.url}")
    private String url;
    public List<Address> getFromOriginal() throws Exception{
        List<Address> resultedList = new ArrayList<>();
        String resultStr = "";
        try (
             CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(this.url));
        ) {
            HttpEntity entity = response.getEntity();
            resultStr = EntityUtils.toString(entity);
        }

        String[] resultStrSplitted= resultStr.split("\n");

        for(String currentLine: resultStrSplitted) {
            String[] currentLineSplitted = currentLine.split(",");

            resultedList.add(Address.builder()
                            .state(currentLineSplitted[0])
                            .city(currentLineSplitted[1])
                            .district(currentLineSplitted[2])
                            .zipcode(StringUtils.leftPad(currentLineSplitted[3], 8, "0"))
                            .street(currentLineSplitted.length > 4 ? currentLineSplitted[4] : null)
                            .build());
        }

        return resultedList;
    }
}
