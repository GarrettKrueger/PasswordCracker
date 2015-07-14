
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crack {
	
	public static void hxCrack(String hexName) throws NoSuchAlgorithmException, IOException{
		File dict = new File("c:/Assignment_3_files/dic-0294.txt");
		
	    String passwordHex = getPasswordHex(hexName);
	    	
	    System.out.println("password hex is: " + passwordHex);
	    int bLength = passwordHex.length()/2;
	    System.out.println("It has " + bLength + " bytes");
	    String algo = getAlgorithm(bLength);		
	    if (algo.equals("none")){
	    	System.out.println("I didn't prepare for this. Byte leangth unexpected, no algorithm found. Error");
	    	return;
	    }
	    System.out.println("we will use " + algo + " algorithm\n");
	    
	    MessageDigest digest = MessageDigest.getInstance(algo);
	    FileReader dictRead = new FileReader(dict);
	    BufferedReader buffDict = new BufferedReader(dictRead);
	    byte[] salt = getSalt();
	    String tPassword;

	    while((tPassword = buffDict.readLine()) != null){
	    	byte[] password = tPassword.getBytes("UTF-8");
	    		
	    	byte[] hashedBytes = digest.digest(password);
	    	byte[] ShashedBytes = digest.digest(concat(password, salt));
	    	byte[] hashedBytesS = digest.digest(concat(salt, password));
	    		
	    	String hashedDict = convertByteArrayToHexString(hashedBytes);
	    	if (hashedDict.equals(passwordHex.trim())){
	    		System.out.println("Password: " + tPassword);
	    		System.out.println("No Salt added\n");
		        break;
	    	}
		    String sHDict = convertByteArrayToHexString(ShashedBytes);
		    if (sHDict.equals(passwordHex.trim())){
		    	System.out.println("Password: " + tPassword);
		    	System.out.println("With salt added at the front\n");
		        break;
		    }
		    String hDictS = convertByteArrayToHexString(hashedBytesS);
		        
		    if (hDictS.equals(passwordHex.trim())){
		        System.out.println("Password: " + tPassword);
		        System.out.println("With salt added at the back\n");
		        break;
		    }
	    }
	    buffDict.close();
	    dictRead.close();
	}
	
	
	private static String getPasswordHex(String pwHexFile){
    	String passwordHex = "";
		try{
			File hexDoc = new File("c:/Assignment_3_files/" + pwHexFile + ".hex");
			FileReader fileRead = new FileReader(hexDoc);
			BufferedReader buffRead = new BufferedReader(fileRead);
			String hex;
			while ((hex = buffRead.readLine()) != null){
				passwordHex = hex;
			}
			fileRead.close();
			buffRead.close();
		} catch (IOException e){
	    	e.printStackTrace(System.out);
	    }
    	return passwordHex;
	}
	
	private static byte[] getSalt(){
		File saltFile = new File("c:/Assignment_3_files/salt.hex");	
		String saltToByte = "";
	    try{
	    	FileReader saltRead = new FileReader(saltFile);
	    	BufferedReader sbuffRead = new BufferedReader(saltRead);
	    	String saltHex;
	    	while ((saltHex = sbuffRead.readLine()) != null){
	    		saltToByte = saltHex;
	    	}
	    	sbuffRead.close();
	    } catch (IOException e){
	    	e.printStackTrace(System.out);
	    }
	    int len = saltToByte.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2){
	        data[i/2] = (byte)((Character.digit(saltToByte.charAt(i), 16) << 4)
	                            + Character.digit(saltToByte.charAt(i+1), 16));
	    }
	    return data;
	}
	
	private static String getAlgorithm(int _bytes){
	    switch(_bytes){
    	case 16:
    		return ("MD5");
    	case 20:
    		return ("SHA-1");
    	case 32: 
    		return ("SHA-256");
    	case 64:
    		return ("SHA-512");
    	default:
    		return ("none") ;
    	}
	}
	
	private static String convertByteArrayToHexString(byte[] arrayBytes){
	    StringBuffer stringBuffer = new StringBuffer();
	    for (int i = 0; i < arrayBytes.length; i++) {
	        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));      
	    }
	    return stringBuffer.toString();
	}
	
	private static byte[] concat(byte[]...arrays){
	    int totalLength = 0;
	    for (int i = 0; i < arrays.length; i++){
	        totalLength += arrays[i].length;
	    }
	    byte[] result = new byte[totalLength];
	    int currentIndex = 0;
	    for (int i = 0; i < arrays.length; i++){
	        System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
	        currentIndex += arrays[i].length;
	    }
	    return result;
	}

}
