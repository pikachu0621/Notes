package com.pikachu.record.tool;


import android.graphics.Bitmap;

/**
 * 读取 和 复制 图片
 */
public class ToolSdImage {


    /**
     * 读取Sd卡图片 返回Bitmap
     *
     * @param path  路径为 "SD卡路径"+"Android/data/com.pikachu.record/files/image/"+  输入的 path
     *              例：输入的path为  a4dt7dg8ya9v1so2
     *                 完整路径为 /storage/emulated/0/Android/data/com.pikachu.record/files/image/a4dt7dg8ya9v1so2
     * @return
     */
    public static Bitmap redImage(String path){




        return null;
    }


    /**
     * 复制Sd卡图片 返回boolean
     *
     * @param path  path为要复制文件的路径  （完整的图片路径 "SD卡路径"+图片路径）
     *             复制到  "SD卡路径"+"Android/data/com.pikachu.record/files/image/"+要复制文件的MD5值
     *
     * @return
     */
    public static boolean copyImage(String path){



        return false;
    }


    /**
     * 获取 文件MD5值
     *
     * @param path （完整的文件路径  "SD卡路径"+图片路径）
     * @return
     */
    public static String getImageMD5(String path){



        return null;
    }











}
