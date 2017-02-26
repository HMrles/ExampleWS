package com.hmrles.ws;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

/**
 * Created by HMrles on 26/01/17.
 */
public class SoapClient{

    /**
     * Metodo para crear el SOAP Request
     */
    private static SOAPMessage createSOAPRequest() throws Exception{
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "http://ws.cdyne.com/AdvancedVerifyEmail");


        /****************************************************************************************************************
         *<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.cdyne.com/">
         *   <soapenv:Header/>
         *   <soapenv:Body>
         *      <ws:AdvancedVerifyEmail>
         *         <!--Optional:-->
         *         <ws:email>vcontreras@gmail.com</ws:email>
         *         <ws:timeout>1</ws:timeout>
         *         <!--Optional:-->
         *         <ws:LicenseKey>rfvi</ws:LicenseKey>
         *      </ws:AdvancedVerifyEmail>
         *   </soapenv:Body>
         *</soapenv:Envelope>
         ***************************************************************************************************************/

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ws", "http://ws.cdyne.com/");

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("AdvancedVerifyEmail", "ws");

        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("email", "ws");
        soapBodyElem1.addTextNode("vcontreras@gmail.com");

        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("timeout", "ws");
        soapBodyElem3.addTextNode("1");

        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("LicenseKey", "ws");
        soapBodyElem4.addTextNode("rfvi");



        soapMessage.saveChanges();


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        soapMessage.writeTo(stream);
        String message = new String(stream.toByteArray(), "utf-8");



        System.out.println(message);

        return soapMessage;
    }



    /**
     * Metodo para imprimir el Soap response
     */
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.println("\nResponse SOAP Message ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }



    /**
     * TestCliente
     */
    public static void main(String args[]){


        try{
            // Crea SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            //Se envia el  SOAP Message a SOAP Server
            String url = "http://ws.cdyne.com/emailverify/Emailvernotestemail.asmx";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

            //Imprimimos la respuesta
            printSOAPResponse(soapResponse);

            soapConnection.close();

        }catch (Exception e){
            System.err.println("Ocurri\\u00f3 un error al enviar el request al servidor");
            e.printStackTrace();
        }
    }
}
