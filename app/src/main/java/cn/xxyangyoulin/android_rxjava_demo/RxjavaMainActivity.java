package cn.xxyangyoulin.android_rxjava_demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mnnyang.advance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class RxjavaMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        observableDemo();
//        subjectDemo();
//        schedulerDemo();
        mapDemo();
//        flatMapDemo();
        distinctDemo();
    }

    void observableDemo() {
        Observable<String> sender;
        //create
        sender = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hahh");
            }
        });

        //just
        sender = Observable.just("just1", "just2");

        //from
        /** 注意，just()方法也可以传list，但是发送的是整个list对象，而from（）发送的是list的一个item*/
        List<String> list = new ArrayList<>();
        list.add("from1");
        list.add("from2");
        list.add("from3");
        sender = Observable.from(list);

        //defer
        /**有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable：*/
        sender = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("deferObservable1", "deferObservable2");
            }
        });

        //interval
        /**创建一个按固定时间间隔发射整数序列的Observable，可用作定时器：**/
        Observable<Long> senderLong;
        senderLong = Observable.interval(1, TimeUnit.SECONDS);

        //range()
        /**发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，
         * 如果为0则不发送，负数则抛异常*/
        Observable<Integer> senderInt;
        senderInt = Observable.range(10, 5);


        //timer()
        /**它在一个给定的延迟后发射一个特殊的值，等同于Android中Handler的postDelay( )方法*/
        senderLong = Observable.timer(3, TimeUnit.SECONDS);

        //repeat() 重复发送他特定值
        sender = Observable.just("repeat").repeat(3);//重复三次

        Observer<String> receiver = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        Observer<Integer> receiverInt = new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer + "++++");
            }
        };

        Observer<Long> receiverLong = new Observer<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }


        };

//        senderLong.subscribe(receiverLong);
//        senderInt.subscribe(receiverInt);
        sender.subscribe(receiver);

       /* sender.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });*/
    }

    void subjectDemo() {
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("ddd1");
        asyncSubject.onNext("ddd2");
        asyncSubject.onNext("ddd3");

        /**AsyncSubject要手动调用onCompleted()*/
        asyncSubject.onCompleted();

        asyncSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d("----", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("----", "onError");
            }

            @Override
            public void onNext(String s) {
                Log.d("----", "onNext" + s);
            }
        });
        /**Observer只会接收asyncSubject的onCompleted()被调用前的最后一个数据，即“asyncSubject3”，
         * 如果不调用onCompleted()，Subscriber将不接收任何数据。*/

        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("default");
        behaviorSubject.onNext("behaviorSubject1");
        behaviorSubject.onNext("behaviorSubject2");
        behaviorSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

                LogUtil.log("behaviorSubject:complete");
            }

            @Override
            public void onError(Throwable e) {

                LogUtil.log("behaviorSubject:error");
            }

            @Override
            public void onNext(String s) {

                LogUtil.log("behaviorSubject:" + s);
            }
        });

        behaviorSubject.onNext("behaviorSubject3");
        behaviorSubject.onNext("behaviorSubject4");

        /***以上代码，Observer会接收到behaviorSubject2、behaviorSubject3、behaviorSubject4，如果在behaviorSubject.subscribe()之前不发送behaviorSubject1、behaviorSubject2，则Observer会先接收到default,再接收behaviorSubject3、behaviorSubject4。*/

        //PublishSubject
        /**PublishSubject比较容易理解，相对比其他Subject常用，它的Observer只会接收到PublishSubject被订阅之后发送的数据。示例代码如下：*/
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("publishSubject1");
        publishSubject.onNext("publishSubject2");
        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.log("publishSubject observer1:" + s);
            }
        });
        publishSubject.onNext("publishSubject3");
        publishSubject.onNext("publishSubject4");
        /**以上代码，Observer只会接收到"behaviorSubject3"、"behaviorSubject4"。*/


        ReplaySubject<String> replaySubject = ReplaySubject.create(); //创建默认初始缓存容量大小为16的ReplaySubject，当数据条目超过16会重新分配内存空间，使用这种方式，不论ReplaySubject何时被订阅，Observer都能接收到数据
//replaySubject = ReplaySubject.create(100);//创建指定初始缓存容量大小为100的ReplaySubject
//replaySubject = ReplaySubject.createWithSize(2);//只缓存订阅前最后发送的2条数据
//replaySubject=ReplaySubject.createWithTime(1,TimeUnit.SECONDS,Schedulers.computation());  //replaySubject被订阅前的前1秒内发送的数据才能被接收
        replaySubject.onNext("replaySubject:pre1");
        replaySubject.onNext("replaySubject:pre2");
        replaySubject.onNext("replaySubject:pre3");
        replaySubject.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtil.log("replaySubject:" + s);
            }
        });
        replaySubject.onNext("replaySubject:after1");
        replaySubject.onNext("replaySubject:after2");
    }

    void schedulerDemo() {
        /**简单粗暴的解释一下，subscribeOn( )决定了发射数据在哪个调度器上执行，observeOn(AndroidSchedulers.mainThread())则指定数据接收发生在UI线程，简直不要太方便。*/
        Observable.create(new Observable.OnSubscribe<ArrayList<String>>() {
            @Override
            public void call(Subscriber<? super ArrayList<String>> subscriber) {
                //数据库获取数据
                //模拟
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < 100; i++) {
                    arrayList.add("fuck++++++" + i);
                }
                subscriber.onNext(arrayList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<String>>() {
                    @Override
                    public void call(ArrayList<String> strings) {
                        for (String string : strings) {
                            System.out.println("8 " + string);
                        }
                    }
                });
    }

    /**
     * 操作符
     */
    private void mapDemo() {
        /**例一：数据类型转换，改变最终的接收的数据类型。假设传入本地图片路径，根据路径获取图片的Bitmap。**/
        String filePath = "";
        Observable.just(filePath).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                return getBitMapByPath();
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                //获取到bitmap显示
            }
        });

        /**例二：对数据进行预处理，最后得到理想型数据。实际开发过程中，从后台接口获取到的数据也许不符合我们想要的，这时候可以在获取过程中对得到的数据进行预处理（结合Retrofit）。*/
        Observable.just("12345678").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s.substring(0, 4);//只要前四位
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i("mytag", s);
            }
        });
    }

    void flatMapDemo() {
        /**和Map很像但又有所区别，Map只是转换发射的数据类型，而FlatMap可以将原始Observable转换成另一个Observable。还是举例说明吧。假设要打印全国所有学校的名称，可以直接用Map：
         为了更清晰一点，先贴一下School类：*/
        List<School> schoolList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            School school = new School();
            school.setName("school" + i);
            ArrayList<School.Student> students = new ArrayList<>();
            for (int i1 = 0; i1 < 3; i1++) {
                students.add(new School.Student().setName("student" + i + "-" + i1));
            }
            school.setStudentList(students);
            schoolList.add(school);
        }

        Observable.from(schoolList).map(new Func1<School, String>() {
            @Override
            public String call(School school) {
                return school.getName();
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String schoolName) {
                Log.i("mytag", schoolName);
            }
        });

        /*Observable.from(schoolList).map(new Func1<School, School.Student>() {
            @Override
            public School.Student call(School school) {
                return school.getStudentList();
                //错误，Student 是一个对象，返回的却是一个list
                //因为Map是一对一的关系，
            }}).subscribe(new Action1<School.Student>() {
            @Override
            public void call(School.Student student) {

                Log.i("mytag",student.getName());
            }});*/
        /**如果我们能利用from()操作符把school.getStudentList()变成另外一个Observable问题不就迎刃而解了吗，这时候就该FlatMap上场了*/
        Observable.from(schoolList).flatMap(new Func1<School, Observable<School.Student>>() {
            @Override
            public Observable<School.Student> call(School school) {
                return Observable.from(school.getStudentList());////关键，将学生列表以另外一个Observable发射出去
            }
        }).subscribe(new Action1<School.Student>() {
            @Override
            public void call(School.Student student) {
                Log.i("mytag", student.getName());
            }
        });


    }

    void bufferDemo() {
        /**缓存，可以设置缓存大小，缓存满后，以list的方式将数据发送出去；例：***/
        Observable.just(1, 2, 3).buffer(2).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> list) {
                Log.i("mytag", "size:" + list.size());
            }
        });
        /*
        * 11-02 20:49:58.370 23392-23392/? I/mytag: size:2
        * 11-02 20:49:58.370 23392-23392/? I/mytag: size:1
        * */

        /*
        * 在开发当中，个人经常将Buffer和Map一起使用，常发生在从后台取完数据，对一个List中的数据进行预处理后，再用Buffer缓存后一起发送，保证最后数据接收还是一个List，如下：
        * */
        List<School> schoolList = new ArrayList<>();
        Observable.from(schoolList).map(new Func1<School, School>() {
            @Override
            public School call(School school) {
                school.setName("NB大学");//改名字
                return school;
            }
        }).buffer(schoolList.size())////缓存起来，最后一起发送
                .subscribe(new Action1<List<School>>() {
                    @Override
                    public void call(List<School> schools) {

                    }
                });
    }

    void takeAndOtherDemo() {
        //take
        /**发射前n项数据，还是用上面的例子，假设不要改所有学校的名称了，就改前四个学校的名称：*/
        List<School> schoolList = new ArrayList<>();
        Observable.from(schoolList).take(4).map(new Func1<School, School>() {
            @Override
            public School call(School school) {
                school.setName("NB大学");
                return school;
            }
        }).buffer(4).subscribe(new Action1<List<School>>() {
            @Override
            public void call(List<School> schools) {
            }
        });
    }

    void distinctDemo() {
        //Distinct  去掉重复的项，比较好理解：
        Observable.just(1, 2, 1, 1, 2, 3)
                .distinct()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer item) {
                        System.out.println("Next: " + item);
                    }
                });

        /*
        06-29 23:14:42.194 10763-10763/? I/System.out: Next: 1
        06-29 23:14:42.194 10763-10763/? I/System.out: Next: 2
        06-29 23:14:42.195 10763-10763/? I/System.out: Next: 3
        * */
    }

    void filterDemo(){
        //Filter：过滤，通过谓词判断的项才会被发射，例如，发射小于4的数据：
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer item) {
                        return( item < 4 );
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer item) {
                System.out.println("Next: " + item);
            }});
        /*
        * Next: 1
          Next: 2
          Next: 3
        * */
    }


    private Bitmap getBitMapByPath() {
        return null;
    }

    private static class LogUtil {

        public static void log(String s) {
            Log.d("----", s);
        }
    }
}
