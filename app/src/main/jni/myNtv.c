//#include <jni.h>
//#include "libavcodec/avcodec.h"
//#include <android/log.h>
//
//JNIEXPORT jstring JNICALL
//Java_com_fxqyem_act_SubActivity_stringFromJNI(JNIEnv *env, jobject instance){
//
//    __android_log_write(ANDROID_LOG_DEBUG, "myNtv.c","Java_com_fxqyem_act_SubActivity_stringFromJNI running---------------------------");
//
//
//    char* vbuf[1024];
//    sprintf(vbuf,"%d\n%s",avcodec_version(),avcodec_configuration());
//
//    return (*env)->NewStringUTF(env,vbuf);//生成java字符串
//}



//
//                                            char buffer[128];
//                                            char  *pBuf=buffer;
//                                            //将java字符串转换为C原生字符串
//
//                                            jboolean  isCopy; // 返回JNI_TRUE表示原字符串的拷贝，返回JNI_FALSE表示返回原字符串的指针
//                                            const jchar *src=(*env)->GetStringCritical(env,js,&isCopy);
//                                            printf("isCopy =%d\n",isCopy);
//
//                                            //C原生的字符串
//                                            char *c="I'm from Jnilib";
//                                            if (src==NULL)
//                                            return (*env)->NewStringUTF(env,c);
//                                            //java类型字符串转换为C类型字符串后
//                                            printf("javaString: \t%s",src);
//                                            //字符拼接错误
//                                            /* size_t length=strlen(src)+strlen(c);
//                                             memcpy(realloc(src,length+1),c,strlen(c));
//                                             *(src+length)='\0';*/
//                                            //字符拼接
//                                            //size_t  length=strlen(src)+strlen(c);
//                                            //memcpy(realloc(src,length+1), c, strlen(c));
//                                            //*(src+length)='\0';
//                                            printf("compare src+ javaString: %s",src);
//                                            while(*src!='\0'){
//                                            *pBuf++=*src++;
//                                            }
//                                            while(*c!='\0'){
//                                            *pBuf++=*c++;
//                                            }
//                                            *pBuf='\0';
//                                            //释放GetStringChars申请的空间
//                                            (*env)->ReleaseStringCritical(env,js,src);
//                                            free(*c);
//                                            jstring  javaString=(*env)->NewStringUTF(env,buffer);
//                                            return javaString;

