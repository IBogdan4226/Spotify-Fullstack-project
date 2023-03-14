package com.ac.tuiasi.mongo_spring.soapPackage.Service;
import com.ac.tuiasi.mongo_spring.soapcosumer.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;

public class SoapService extends WebServiceGatewaySupport {

    public boolean validateTokenClient(String jwtToken){
        String actualToken=jwtToken.substring(7,jwtToken.length());
        String authResponse;
        authResponse=this.getAuthResponse(actualToken).getAuthorizationResult().getValue();
        if(authResponse.equals("Token invalid")){
            return false;
        }
        if(!(Integer.parseInt(authResponse.split("-")[1])>=1)){
            return false;
        }
        return true;
    }

    public boolean validateTokenPlaylist(String jwtToken,Integer creatorId){
        String actualToken=jwtToken.substring(7,jwtToken.length());
        String authResponse;
        authResponse=this.getAuthResponse(actualToken).getAuthorizationResult().getValue();
        if(authResponse.equals("Token invalid")){
            return false;
        }
        if(authResponse.split("-")[1].equals("4")||creatorId==Integer.parseInt(authResponse.split("-")[0])){
            return true;
        }
        return false;
    }



    private AuthorizationResponse getAuthResponse(String jwtToken){
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<String> jaxbElement = objectFactory.createAuthorizationJwtToVerify(jwtToken);
        Authorization request=new Authorization();
        request.setJwtToVerify(jaxbElement);
        JAXBElement<Authorization> jaxbRequest=objectFactory.createAuthorization(request);
        JAXBElement<AuthorizationResponse> response=(JAXBElement<AuthorizationResponse>)  getWebServiceTemplate().marshalSendAndReceive("http://localhost:8079",jaxbRequest,new SoapActionCallback("authorize"));
        return response.getValue();
    }
}
