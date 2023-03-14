package com.ac.tuiasi.spotify.soapPackage.configuration;

import com.ac.tuiasi.spotify.soapPackage.Service.SoapService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class SoapConfiguration {
    private final String uri = "http://localhost:8079";

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.ac.tuiasi.spotify.soapcosumer");
        return marshaller;
    }

    @Bean
    public SoapService authorizationClientSoap(Jaxb2Marshaller marshaller) {
        SoapService client = new SoapService();
        client.setDefaultUri(this.uri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
