package com.ac.tuiasi.spotify.soapPackage.Service;

import com.ac.tuiasi.spotify.soapcosumer.Authorization;
import com.ac.tuiasi.spotify.soapcosumer.AuthorizationResponse;
import com.ac.tuiasi.spotify.soapcosumer.ObjectFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;

public class SoapService extends WebServiceGatewaySupport {

    public boolean validateTokenCM(String jwtToken) {
        String actualToken = jwtToken.substring(7);
        String authResponse;
        authResponse = this.getAuthResponse(actualToken).getAuthorizationResult().getValue();
        if (authResponse.equals("Token invalid")) {
            return false;
        }
        return authResponse.split("-")[1].equals("4");
    }

    public boolean validateTokenMinArtist(String jwtToken) {
        String actualToken = jwtToken.substring(7);
        String authResponse;
        authResponse = this.getAuthResponse(actualToken).getAuthorizationResult().getValue();
        if (authResponse.equals("Token invalid")) {
            return false;
        }
        return !authResponse.split("-")[1].equals("1");
    }

    public boolean validateTokenArtist(String jwtToken, Integer artistId) {
        String actualToken = jwtToken.substring(7);
        String authResponse;
        authResponse = this.getAuthResponse(actualToken).getAuthorizationResult().getValue();
        if (authResponse.equals("Token invalid")) {
            return false;
        }

        return authResponse.split("-")[1].equals("4") || Integer.parseInt(authResponse.split("-")[0]) == artistId;
    }


    private AuthorizationResponse getAuthResponse(String jwtToken) {
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<String> jaxbElement = objectFactory.createAuthorizationJwtToVerify(jwtToken);
        Authorization request = new Authorization();
        request.setJwtToVerify(jaxbElement);
        JAXBElement<Authorization> jaxbRequest = objectFactory.createAuthorization(request);
        JAXBElement<AuthorizationResponse> response = (JAXBElement<AuthorizationResponse>) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8079", jaxbRequest, new SoapActionCallback("authorize"));
        return response.getValue();
    }
}
