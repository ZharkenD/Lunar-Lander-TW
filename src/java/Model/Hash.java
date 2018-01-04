package Model;

public class Hash {
 
    /**
     * 
     * @param textToHash
     * @param hashType
     * @return 
     */
    public static String getHash(String textToHash, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            byte[] array = md.digest(textToHash.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
 
    /**
     * 
     * @param textToHash
     * @return 
     */
    public static String md5(String textToHash) {
        return Hash.getHash(textToHash, "MD5");
    }
 
    /**
     * 
     * @param textToHash
     * @return 
     */
    public static String sha1(String textToHash) {
        return Hash.getHash(textToHash, "SHA1");
    }
 
}
