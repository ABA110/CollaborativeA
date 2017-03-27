// do not modify this class

import javax.xml.soap.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.dom.*;
import java.io.*;

public class SOAPClient {

    public static String sendMess(String url, String mess) throws Exception {
		SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(mess), url);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
        soapConnection.close();
		String resp = new String(out.toByteArray());	
		return resp;
    }

    private static SOAPMessage createSOAPRequest(String m) throws Exception {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(m.getBytes()));
		soapMessage.getSOAPPart().setContent(new DOMSource(document));
		soapMessage.saveChanges();
        return soapMessage;
    }

}