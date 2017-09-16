#include <jni.h>
#include <string>
#include "dalvik.h"

typedef Object *(*FindObject)(void *thead, jobject jobject1);

typedef void *(*FindThread)();

FindObject findObject;
FindThread findThread;
extern "C"{
/*
 * Class:     com_example_emery_andfixframwork_FixManager
 * Method:    replace
 * Signature: (Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V
 */
JNIEXPORT void JNICALL Java_com_example_emery_andfixframwork_FixManager_replace(
        JNIEnv *env, jobject instance, jint sdkVersion, jobject errMethod, jobject rigMethod) {

    Method *errorMethod = (Method *) env->FromReflectedMethod(errMethod);
    Method *rightMethod =(Method *) env->FromReflectedMethod(rigMethod);

    void *dvm_hand = dlopen("libdvm.so", RTLD_NOW);
    findObject = (FindObject) dlsym(dvm_hand, sdkVersion > 10 ? "_Z20dvmDecodeIndirectRefP6ThreadP8_jobject" : "dvmDecodeIndirectRef");
    findThread = (FindThread) dlsym(dvm_hand, sdkVersion > 10 ? "_Z13dvmThreadSelfv":"dvmThreadSelf");

    jclass methodClazz = env->FindClass("java/lang/reflect/Method");
    jmethodID rightMethodID = env->GetMethodID(methodClazz, "getDeclaringClass", "()Ljava/lang/Class;");
    jobject ndkObject = env->CallObjectMethod(rigMethod, rightMethodID);

    ClassObject *firstField = (ClassObject *) findObject(findThread(), ndkObject);
    firstField->status = CLASS_INITIALIZED;
    errorMethod->accessFlags |= ACC_PUBLIC;
    errorMethod->methodIndex=rightMethod->methodIndex;
    errorMethod->jniArgInfo=rightMethod->jniArgInfo;
    errorMethod->registersSize=rightMethod->registersSize;
    errorMethod->outsSize=rightMethod->outsSize;
    errorMethod->prototype=rightMethod->prototype;
    errorMethod->insns=rightMethod->insns;
}
}