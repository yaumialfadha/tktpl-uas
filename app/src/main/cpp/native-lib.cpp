#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_yaumialfadha_easysplitbill_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_yaumialfadha_easysplitbill_views_BillHomeAdapter_rupiah(
        JNIEnv* env,
        jobject,
        jcharArray nomor) {

    jchar* inCArray = env->GetCharArrayElements(nomor, NULL);
    if (NULL == inCArray) return NULL;
    jsize length = env->GetArrayLength(nomor);
    int j = 0;
    std::string str = "";
    for (int i = length-1; i >= 0; i--) {
        if (j == 2 && i != 0) {
            str = "." + std::to_string(inCArray[i]-48) + str;
            j = 0;
        } else {
            str = std::to_string(inCArray[i]-48) + str;
            j += 1;
        }
    }
    std::string res = "Rp " + str + ",00";
    return env->NewStringUTF(res.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_yaumialfadha_easysplitbill_views_BillWithPersonAdapter_rupiah(
        JNIEnv* env,
        jobject,
        jcharArray nomor) {

    jchar* inCArray = env->GetCharArrayElements(nomor, NULL);
    if (NULL == inCArray) return NULL;
    jsize length = env->GetArrayLength(nomor);
    int j = 0;
    std::string str = "";
    for (int i = length-1; i >= 0; i--) {
        if (j == 2 && i != 0) {
            str = "." + std::to_string(inCArray[i]-48) + str;
            j = 0;
        } else {
            str = std::to_string(inCArray[i]-48) + str;
            j += 1;
        }
    }
    std::string res = "Rp " + str + ",00";
    return env->NewStringUTF(res.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_yaumialfadha_easysplitbill_views_BillDetailFragment_rupiah(
        JNIEnv* env,
        jobject,
        jcharArray nomor) {

    jchar* inCArray = env->GetCharArrayElements(nomor, NULL);
    if (NULL == inCArray) return NULL;
    jsize length = env->GetArrayLength(nomor);
    int j = 0;
    std::string str = "";
    for (int i = length-1; i >= 0; i--) {
        if (j == 2 && i != 0) {
            str = "." + std::to_string(inCArray[i]-48) + str;
            j = 0;
        } else {
            str = std::to_string(inCArray[i]-48) + str;
            j += 1;
        }
    }
    std::string res = "Rp " + str + ",00";
    return env->NewStringUTF(res.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_yaumialfadha_easysplitbill_views_PersonWithFoodsAdapter_rupiah(
        JNIEnv* env,
        jobject,
        jcharArray nomor) {

    jchar* inCArray = env->GetCharArrayElements(nomor, NULL);
    if (NULL == inCArray) return NULL;
    jsize length = env->GetArrayLength(nomor);
    int j = 0;
    std::string str = "";
    for (int i = length-1; i >= 0; i--) {
        if (j == 2 && i != 0) {
            str = "." + std::to_string(inCArray[i]-48) + str;
            j = 0;
        } else {
            str = std::to_string(inCArray[i]-48) + str;
            j += 1;
        }
    }
    std::string res = "Rp " + str + ",00";
    return env->NewStringUTF(res.c_str());
}