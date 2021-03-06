# Shaft



### 所有的列表均继承自BaseListFragment 这样一来将会大大减少代码量，子类只需要这样
```java

public class FragmentIllustList extends BaseListFragment<ListIllustResponse, IllustStagAdapter, IllustsBean> {

    @Override
    Observable<ListIllustResponse> initApi() {
        return Retro.getAppApi().getRank(mUserModel.getResponse().getAccess_token(), "day_male");
    }

    @Override
    Observable<ListIllustResponse> initNextApi() {
        return Retro.getAppApi().getNextIllust(mUserModel.getResponse().getAccess_token(), nextUrl);
    }

    @Override
    void initAdapter() {
        mAdapter = new IllustStagAdapter(allItems, mContext);
    }
}
```
### 管道
```java
    public static class IllustPip<Target>{

        private List<Target> beans = new ArrayList<>();

        List<Target> getBeans() {
            return beans;
        }

        void setBeans(List<Target> beans) {
            this.beans = beans;
        }
    }
```
### 本地存储
```java
    public static <T> void getLocalIllust(Callback<List<T>> callback) {
        IllustPip<T> pip = new IllustPip<>();
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("开始读取本地文件");
            Common.showLog("Observable thread is : " + Thread.currentThread().getName());
            FileInputStream fis = Shaft.getContext().openFileInput("RecommendIllust");//获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            pip.setBeans((List<T>) ois.readObject());
            fis.close();
            ois.close();
            emitter.onNext("本地文件读取完成");
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Common.showLog(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Common.showToast(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        callback.doSomething(pip.getBeans());
                    }
                });
    }
```




######有亮眼的设计的同学，请联系fatemercis@qq.com或留下链接/截图，十分感谢
![截图](https://raw.githubusercontent.com/CeuiLiSA/Shaft/master/snap/Screenshot_1554187583.png)
