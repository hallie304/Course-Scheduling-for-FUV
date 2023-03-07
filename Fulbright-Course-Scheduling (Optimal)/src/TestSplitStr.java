import java.util.*;

public class TestSplitStr {
    public static void main(String[] args) {
        String a = "Phan Thanh Trung & Nguyen Nam & Graeme Walker";
        String[] splitStr = a.split("&");
        if(splitStr.length > 1) {
            // Đầu
            splitStr[0] = splitStr[0].substring(0, splitStr[0].length() - 1);
            
            // Giữa
            for(int i = 1; i < splitStr.length - 1; i++)
                splitStr[i] = splitStr[i].substring(1, splitStr[i].length() - 1);
            
            // Cuối
            int lastId = splitStr.length - 1;
            splitStr[lastId] = splitStr[lastId].substring(1);

        }

        System.out.println(Arrays.toString(splitStr));
    }
}
