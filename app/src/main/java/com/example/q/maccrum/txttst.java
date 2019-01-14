package com.example.q.maccrum;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

/*
 * Gson: https://github.com/google/gson
 * Maven info:
 *     groupId: com.google.code.gson
 *     artifactId: gson
 *     version: 2.8.1
 *
 * Once you have compiled or downloaded gson-2.8.1.jar, assuming you have placed it in the
 * same folder as this file (GetKeyPhrases.java), you can compile and run this program at
 * the command line as follows.
 *
 * javac GetKeyPhrases.java -classpath .;gson-2.8.1.jar -encoding UTF-8
 * java -cp .;gson-2.8.1.jar GetKeyPhrases
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class Document {
    public String id, language, text;

    public Document(String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
}

class Documents {
    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<Document>();
    }
    public void add(String id, String language, String text) {
        this.documents.add (new Document (id, language, text));
    }
}

class GetKeyPhrases {

// ***********************************************
// *** Update or verify the following values. ***
// **********************************************

// Replace the accessKey string value with your valid access key.
    static String accessKey = "caaa5a0ea71a445984cd7d15218ef3a9";

// Replace or verify the region.

// You must use the same region in your REST API call as you used to obtain your access keys.
// For example, if you obtained your access keys from the westus region, replace
// "westcentralus" in the URI below with "westus".

// NOTE: Free trial access keys are generated in the westcentralus region, so if you are using
// a free trial access key, you should not need to change this region.
    static String host = "https://koreacentral.api.cognitive.microsoft.com";

    static String path = "/text/analytics/v2.0/keyPhrases";

    public static String GetKeyPhrases (Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host+path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    public static void main (String[] args) {
        try {
            Documents documents = new Documents ();
            documents.add ("1", "ko", "안녕하세요 저는 신인철이고 원래 텐서플로를 이용해서 하기로했는데 파이토치로 옮겨서 지금 하고있는데 기존에 파이토치로 얼굴 학습을 된 거를 인식을 해서 그 테스트 데이터 400장 된거를 하고있는데 학습속도가 너무 손실함수를 보면 너무 빨리떨어지는 현상이 있는데 이것을 해결하려고 하고 있습니다.");
            documents.add ("2", "ko", "저는 이가연이구요 저는 그래서 어제 파이토치로 해봤는데 그게 결과가 아까 처음에 내려가고 나서 일정 정도를 계속 유지를해요 그래서 더 이상 안내려가는데 한 1.07 1.1 사이를 계속 왔다갔다만 해서 실제로 결과를 보니까 그렇게 어 저희가");
            documents.add ("3", "ko", "저는 이동연이라고 하구요 모델 어제 찾은게 너무 어려워서 새로 찾아서 그걸로 돌려봤는데 문제는 그 코드가 이제 데이터를 노멀라이즈하고 그걸 돌리더라구요 그래서 이제 제가 만들고 싶은 것은 원래값을 해서 그거를 보여주고 싶은데");

            String response = GetKeyPhrases (documents);
            System.out.println (prettify (response));
        }
        catch (Exception e) {
            System.out.println (e);
        }
    }
}
