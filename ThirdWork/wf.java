import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javafx.util.Pair;

public class test {
    public static void main(String[] args) {
        switch (args[0]){
            case "-c":
                File f = new File(args[1]);
                printWordFrequency(f);
                break;
            case "-f":
                printWords();
                break;
            case "-d":
                printWordsInDir();
                break;
            case "-d -s":
                printWordsInDir_recursive();
                break;
            case "-n":
                printHighFrequencyWords();
                break;
            case "-x":
                stopWords();
                break;
            case "-p":
                printPhrase();
                break;
            case "-v":
                uniformVerbForm();
                break;
        }
    }

    public static void printWordFrequency(File f){      //传入一个文件
        System.out.println("输出26个字母的频率:");
        int flu[] = new int[26];
        int num = 0;
        for(int i = 0; i<26; i++)
        {
            flu[i] = 0;
        }
        if(f.exists())
        {
            try {
                FileInputStream fls = new FileInputStream(f);           
                InputStreamReader isr = new InputStreamReader(fls, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                int Ch;
                while((Ch = br.read()) != -1)
                {
                    char c = (char) Ch;
                    c = Character.toLowerCase(c);           //全部切换为小写字母
                    if((c-'a') >= 26 || (c-'a') < 0)
                        continue;
                    else
                    {
                        num++;
                        flu[c-'a']++;
                    }
                }
                br.close();
                ArrayList<Pair<String, Double>> wordFlu = new ArrayList<>();        //使用数组中的键值对存储字母和频率
                for(int i = 0; i<26; i++)
                {
                    char c = (char)(i+'a');
                    String s = String.valueOf(c);
                    Pair<String, Double> pair = new Pair<>(s, Double.valueOf((double)flu[i]/num));
                    wordFlu.add(pair);
                }
                wordFlu.sort(new Comparator() {                         //负责重写sort的匿名内部类
                    @Override
                    public int compare(Object o1, Object o2)        //重写了compare函数
                    {
                        Pair<String, Double> s1 = (Pair)o1;
                        Pair<String, Double> s2 = (Pair)o2;
                        return new Integer(s2.getValue().compareTo(s1.getValue()));
                    }
                });
                System.out.println(f.getName() + "中的字母频率从大到小为：");
                int sum = 0;
                for(int i = 0; i<26; i++)
                {
                    if(sum == 3)
                    {
                        sum = 0;
                        System.out.println();
                    }
                    System.out.format("%s : %.2f   ", wordFlu.get(i).getKey(), wordFlu.get(i).getValue()*100);
                    sum++;
                }
            }
            catch (FileNotFoundException e){
                System.out.println("没有找到该文件！");
                System.exit(1);
            }
            catch (UnsupportedEncodingException e){
                System.out.println("编码错误！");
                System.exit(1);
            }
            catch (IOException e){
                System.out.println("读写错误！");
                System.exit(1);
            }
        }
    }

    public static void printWords(){
        System.out.println("???????????????????????????????????????????????");
    }

    public static void printWordsInDir(){
        System.out.println("???????????????????????????????????????????????????????????????????????????????");
    }

    public static void printWordsInDir_recursive(){
        System.out.println("?????????????????????????????????????????????????????????????");
    }

    public static void printHighFrequencyWords(){
        System.out.println("??????????????????????????????????????????°n????????????????????");
    }

    public static void stopWords(){
        System.out.println("???????????????????¨????????");
    }

    public static void printPhrase(){
        System.out.println("?????????n??????????????????????");
    }

    public static void uniformVerbForm(){
        System.out.println("??????????????????????????????");
    }
}
