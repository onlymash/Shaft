package ceui.lisa.network;

import java.io.IOException;

import ceui.lisa.fragments.FragmentLogin;
import ceui.lisa.utils.Common;
import ceui.lisa.utils.Local;
import ceui.lisa.response.UserModel;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * 全局自动刷新Token的拦截器
 */
public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (isTokenExpired(response)) {
            String newToken = getNewToken();
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("Authorization", newToken)
                    .build();
            return chain.proceed(newRequest);
        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        if (response.code() == 400) {
            return true;
        }
        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    private String getNewToken() throws IOException {
        UserModel userModel = Local.getUser();
        Call<UserModel> call = Retro.getAccountApi().refreshToken(
                FragmentLogin.CLIENT_ID,
                FragmentLogin.CLIENT_SECRET,
                "refresh_token",
                userModel.getResponse().getRefresh_token(),
                userModel.getResponse().getDevice_token(),
                true,
                true);
        UserModel newUser = call.execute().body();
        newUser.getResponse().getUser().setPassword(userModel.getResponse().getUser().getPassword());
        Local.saveUser(newUser);
        if(newUser != null && newUser.getResponse() != null) {
            return newUser.getResponse().getAccess_token();
        }else {
            return "ERROR ON GET TOKEN";
        }
    }
}
