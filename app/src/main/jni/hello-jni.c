#include <jni.h>
//jstring: 返回值类型 String
//规则: Java_包名_类名_方法名（jvm虚拟机的指针，调用者对象）
jstring Java_com_atar_activitys_demos_TestJNIActivity_helloFromC(JNIEnv* env,jobject thiz){
    return (*env)->NewStringUTF(env, "你好,我来自C代码 !!!");
}