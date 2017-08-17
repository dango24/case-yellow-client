package caseyellow.client.sevices.infrastrucre;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitBuilder {


    public static Retrofit.Builder Retrofit(String url) {
        Retrofit.Builder retrofit = new Retrofit.Builder()
                                                .baseUrl(url);

        retrofit.addConverterFactory(JacksonConverterFactory.create());

        return retrofit;
    }
}
