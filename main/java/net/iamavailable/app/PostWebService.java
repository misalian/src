package net.iamavailable.app;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.widget.Toast;
/**
 * Created by Arshad on 3/21/2016.
 */
public class PostWebService {
    private static String NAMESPACE = "http://tempuri.org/";
    //private static String URL = "http://iamavailable.cust-fyp-projects.com/IamavailableService.asmx";
    private static String URL = "http://cust-fyp-projects.com/Iamavailable3service.asmx";
    private static String SOAP_ACTION = "http://tempuri.org/";
    static String[] al;
    // Calling pmLogin WEBSERVICE Method of Project Manager

    public static String postDataServer(String username,String frontpic,String backpic,String message,String tstart,String tend,String date,String lat,String lang,String statusmood,String email,String imenumber,String city,String country,String frontpicpath,String backpicpath,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("username");
        celsiusPI.setValue(username);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("frontpic");
        celsiusPI.setValue(frontpic);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("backpic");
        celsiusPI.setValue(backpic);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("message");
        celsiusPI.setValue(message);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("tstart");
        celsiusPI.setValue(tstart);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("tend");
        celsiusPI.setValue(tend);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("lat");
        celsiusPI.setValue(lat);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("lang");
        celsiusPI.setValue(lang);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("statusmood");
        celsiusPI.setValue(statusmood);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("imenumber");
        celsiusPI.setValue(imenumber);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        celsiusPI = new PropertyInfo();
        celsiusPI.setName("city");
        celsiusPI.setValue(city);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("country");
        celsiusPI.setValue(country);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("frontpicpath");
        celsiusPI.setValue(frontpicpath);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("backpicpath");
        celsiusPI.setValue(backpicpath);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String[] getSelectedPostServer(String loc,String email,String date,String stime,String webMethName) { //webmethod is the name of method
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("loc");
        celsiusPI.setValue(loc);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("stime");
        celsiusPI.setValue(stime);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapObject response1 = (SoapObject) envelope.bodyIn;
            SoapObject response2 = (SoapObject) response1.getProperty(0);
            al=new String[response2.getPropertyCount()];
            for(int i=0;i<response2.getPropertyCount();i++){
                String a=response2.getProperty(i).toString();
                if(a!="")
                    al[i]=a;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
				/*Toast.makeText(c,
						resTxt,
						Toast.LENGTH_LONG).show();*/
        }

        return al;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String postRating(String numstar,String id,String email,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("numstar");
        celsiusPI.setValue(numstar);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("id");
        celsiusPI.setValue(id);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String[] checkUpdates(String imenumber,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("imenumber");
        celsiusPI.setValue(imenumber);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapObject response1 = (SoapObject) envelope.bodyIn;
            SoapObject response2 = (SoapObject) response1.getProperty(0);
            al=new String[response2.getPropertyCount()];
            for(int i=0;i<response2.getPropertyCount();i++){
                String a=response2.getProperty(i).toString();
                if(a!="")
                    al[i]=a;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
				/*Toast.makeText(c,
						resTxt,
						Toast.LENGTH_LONG).show();*/
        }

        return al;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String postCommetment(String email,String id,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("id");
        celsiusPI.setValue(id);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String[] checkResponse(String imenumber,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("imenumber");
        celsiusPI.setValue(imenumber);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapObject response1 = (SoapObject) envelope.bodyIn;
            SoapObject response2 = (SoapObject) response1.getProperty(0);
            al=new String[response2.getPropertyCount()];
            for(int i=0;i<response2.getPropertyCount();i++){
                String a=response2.getProperty(i).toString();
                if(a!="")
                    al[i]=a;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
				/*Toast.makeText(c,
						resTxt,
						Toast.LENGTH_LONG).show();*/
        }

        return al;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String registerU(String username,String email,String password,String imenumber,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("username");
        celsiusPI.setValue(username);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("password");
        celsiusPI.setValue(password);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("imenumber");
        celsiusPI.setValue(imenumber);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String checkLogin(String email,String password,String regToken,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("password");
        celsiusPI.setValue(password);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("regToken");
        celsiusPI.setValue(regToken);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String forgetEmails(String email,String newpass,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("newpass");
        celsiusPI.setValue(newpass);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String updatePass(String email,String imenumber,String newpass,String oldpass,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("imenumber");
        celsiusPI.setValue(imenumber);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("newpass");
        celsiusPI.setValue(newpass);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("oldpass");
        celsiusPI.setValue(oldpass);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getLikDolrSplash(String email,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String delCurrentPost(String email,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String[] postHistory(String email,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapObject response1 = (SoapObject) envelope.bodyIn;
            SoapObject response2 = (SoapObject) response1.getProperty(0);
            al=new String[response2.getPropertyCount()];
            for(int i=0;i<response2.getPropertyCount();i++){
                String a=response2.getProperty(i).toString();
                if(a!="")
                    al[i]=a;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
				/*Toast.makeText(c,
						resTxt,
						Toast.LENGTH_LONG).show();*/
        }

        return al;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String[] getPHistoryAgaistId(String id,String webMethName) { //webmethod is the name of method
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("id");
        celsiusPI.setValue(id);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapObject response1 = (SoapObject) envelope.bodyIn;
            SoapObject response2 = (SoapObject) response1.getProperty(0);
            al=new String[response2.getPropertyCount()];
            for(int i=0;i<response2.getPropertyCount();i++){
                String a=response2.getProperty(i).toString();
                if(a!="")
                    al[i]=a;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
				/*Toast.makeText(c,
						resTxt,
						Toast.LENGTH_LONG).show();*/
        }

        return al;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String deletePostHistory(String postid,String date,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("postid");
        celsiusPI.setValue(postid);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String[] getPics(String id,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("id");
        celsiusPI.setValue(id);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        //AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapObject response1 = (SoapObject) envelope.bodyIn;
            SoapObject response2 = (SoapObject) response1.getProperty(0);
            al=new String[response2.getPropertyCount()];
            for(int i=0;i<response2.getPropertyCount();i++){
                String a=response2.getProperty(i).toString();
                if(a!="")
                    al[i]=a;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
				/*Toast.makeText(c,
						resTxt,
						Toast.LENGTH_LONG).show();*/
        }

        return al;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String postImage(String imagebytes,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("imagebytes");
        celsiusPI.setValue(imagebytes);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String deleteAllPostHistory(String email,String webMethName) { //webmethod is the name of method
        String resTxt = null;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("email");
        celsiusPI.setValue(email);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }
}
