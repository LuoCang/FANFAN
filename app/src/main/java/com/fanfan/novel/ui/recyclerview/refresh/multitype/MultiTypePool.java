package com.fanfan.novel.ui.recyclerview.refresh.multitype;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fanfan.novel.ui.recyclerview.refresh.base.BaseViewProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2017/12/19.
 */

public class MultiTypePool implements TypePool {
    private final String TAG = MultiTypePool.class.getSimpleName();

    private final List<Class<?>> mContents;
    private final List<BaseViewProvider> mProviders;

    public MultiTypePool() {
        mContents = new ArrayList<>();
        mProviders = new ArrayList<>();
    }

    /**
     * 将数据类型和 provider 关联起来
     *
     * @param clazz    数据类型
     * @param provider provider
     */
    @Override
    public void register(@NonNull Class<?> clazz, @NonNull BaseViewProvider provider) {
        if (!mContents.contains(clazz)) {
            mContents.add(clazz);
            mProviders.add(provider);
        } else {
            int index = mContents.indexOf(clazz);
            mProviders.set(index, provider);
            Log.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
                    "It will override the original binder.");
        }
    }

    @Override
    public int indexOf(@NonNull final Class<?> clazz) throws ProviderNotFoundException {
        int index = mContents.indexOf(clazz);
        if (index >= 0) {
            return index;
        }
        for (int i = 0; i < mContents.size(); i++) {
            //isAssignableFrom 是用来判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口
            if (mContents.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        throw new ProviderNotFoundException(clazz);
    }

    @Override
    public BaseViewProvider getProviderByIndex(int index) {
        return mProviders.get(index);
    }

    @Override
    public <T extends BaseViewProvider> T getProviderByClass(@NonNull final Class<?> clazz) {
        return (T) getProviderByIndex(indexOf(clazz));
    }

    public List<Class<?>> getContents() {
        return mContents;
    }

    public List<BaseViewProvider> getProviders() {
        return mProviders;
    }


    public class ProviderNotFoundException extends RuntimeException {

        public ProviderNotFoundException(@NonNull Class<?> clazz) {
            super("Do you have registered the provider for {className}.class in the adapter/pool?"
                    .replace("{className}", clazz.getSimpleName()));
        }
    }
}
