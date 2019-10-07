import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javafx.util.Pair;

public class wf {
    public static void main(String[] args) {
        switch (args[0]){
            case "-c":
                File f1 = new File(args[1]);
                printWordFrequency(f1);
                break;
            case "-f":
                File f2 = new File(args[1]);
                printWords(f2);
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

    public static void printWords(File f){
        System.out.println("请输入查询单词的数量：");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println("输出前" + n + "个最常见的单词：");
        if(f.exists())
        {
            try
            {
                FileInputStream fls = new FileInputStream(f);
                InputStreamReader isr = new InputStreamReader(fls, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                Map<String, Integer> wordNum = new HashMap<>();
                int ch;
                String s = "";
                while((ch = br.read()) != -1)
                {
                    char c = (char)ch;
                    c = Character.toLowerCase(c);
                    if(c == ' ' || c == '.' || c == ':' || c == '\'')
                    {
                        if(s == "")
                            continue;
                        if(!wordNum.containsKey(s))
                        {
                            wordNum.put(s, 1);
                        }
                        else
                        {
                            wordNum.put(s, wordNum.get(s)+1);
                        }
                        s = "";
                    }
                    else{
                        s += String.valueOf(c);
                    }
                }
                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(wordNum.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
                {
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                    {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                int sum = 0,num = 0;
                for (Map.Entry<String, Integer> map : list) 
                {
                    if(num == n)
                        break;
                    if(sum == 3)
                    {
                        sum = 0;
                        System.out.println();
                    }
                    System.out.print(map.getKey() + "：" + map.getValue() + "次    ");
                    sum++;
                    num++;
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
