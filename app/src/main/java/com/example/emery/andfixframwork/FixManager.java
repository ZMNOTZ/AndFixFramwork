package com.example.emery.andfixframwork;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * Created by emery on 2017/9/10.
 */

public class FixManager {
    private static final FixManager instance=new FixManager();
    private static Context mContext;
    static {
        System.loadLibrary("fix");
    }
    public static FixManager getInstance(Context context){
        mContext = context;

        return instance;
    }
    public void loadFile(File file){
        try {
            //加载dex
            DexFile dexFile=DexFile.loadDex(file.getAbsolutePath(),mContext.getCacheDir().getAbsolutePath()+File.separator+"opt",Context.MODE_PRIVATE);
            //加载class
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()){
                String className = entries.nextElement();
                //获取子节码文件，不能用class.forName(),这个方法只能获取已经安装了的文件
                Class clazz = dexFile.loadClass(className, mContext.getClassLoader());
                if(clazz!=null){
                    fixClazz(clazz);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param clazz
     *  修复文件的.class
     */
    private void fixClazz(Class clazz) throws ClassNotFoundException, NoSuchMethodException {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method rightMethod : declaredMethods) {
            Replace replace = rightMethod.getAnnotation(Replace.class);
            if(replace==null){
                continue;
            }
            //找到了修复好的method  获取出问题的method

            //出问题的类
            String errClassName=replace.jazz();
            //出问题的方法
            String errMethodName=replace.method();
            Class<?> errClazz = clazz.forName(errClassName);
            //rightMethod.getParameterTypes() 肯能有同名方法，但是错误方法应该和正确的方法的参数类型号是一样的。
            Method errMethod = errClazz.getDeclaredMethod(errMethodName, rightMethod
                    .getParameterTypes());
            replace(Build.VERSION.SDK_INT,errMethod,rightMethod);
        }
    }

    /**
     *
     * @param sdkVersion
     * @param errMethod
     * @param rightMethod
     */
    private  native void replace(int sdkVersion,Method errMethod, Method rightMethod);

}
