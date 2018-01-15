package com.fanfan.novel.utils;

import android.app.Activity;
import android.content.res.Resources;

import com.fanfan.robot.R;
import com.fanfan.novel.common.enums.SpecialType;

import java.util.Arrays;

/**
 * Created by android on 2017/12/19.
 */

public class SpecialUtils {

    private static String[] MusicArray = {"唱歌", "唱歌儿", "唱一首歌", "我想听音乐", "播放音乐", "来首歌曲", "播放歌曲", "唱首歌", "音乐", "音乐播放中..."};
    private static String[] StoryArray = {"故事"};
    private static String[] JokeArray = {"笑话"};

    public static SpecialType doesExist(Resources resources, String speakTxt) {
        if (txtInArray(speakTxt, MusicArray) || Arrays.asList(resFoFinal(resources, R.array.other_misic)).contains(speakTxt)) {
            return SpecialType.Music;
        } else if (txtInArray(speakTxt, StoryArray) || Arrays.asList(resFoFinal(resources, R.array.other_story)).contains(speakTxt)) {
            return SpecialType.Story;
        } else if (txtInArray(speakTxt, JokeArray) || Arrays.asList(resFoFinal(resources, R.array.other_joke)).contains(speakTxt)) {
            return SpecialType.Joke;
        } else if (txtInTxt(resources, speakTxt, R.string.Forward)) {
            return SpecialType.Forward;
        } else if (txtInTxt(resources, speakTxt, R.string.Backoff)) {
            return SpecialType.Backoff;
        } else if (txtInTxt(resources, speakTxt, R.string.Turnleft)) {
            return SpecialType.Turnleft;
        } else if (txtInTxt(resources, speakTxt, R.string.Turnright)) {
            return SpecialType.Turnright;
        } else if (txtInTxt(resources, speakTxt, R.string.Logout)) {
            return SpecialType.Logout;
        } else if (txtInTxt(resources, speakTxt, R.string.Map)) {
            return SpecialType.Map;
        } else if (txtInTxt(resources, speakTxt, R.string.StopListener)) {
            return SpecialType.StopListener;
        } else if (txtInTxt(resources, speakTxt, R.string.Video)) {
            return SpecialType.Video;
        } else if (txtInTxt(resources, speakTxt, R.string.Problem)) {
            return SpecialType.Problem;
        } else if (txtInTxt(resources, speakTxt, R.string.MultiMedia)) {
            return SpecialType.MultiMedia;
        } else if (txtInTxt(resources, speakTxt, R.string.Seting_up)) {
            return SpecialType.Seting_up;
        } else if (txtInTxt(resources, speakTxt, R.string.Public_num)) {
            return SpecialType.Public_num;
        } else if (txtInTxt(resources, speakTxt, R.string.Navigation)) {
            return SpecialType.Navigation;
        } else if (txtInTxt(resources, speakTxt, R.string.Face)) {
            return SpecialType.Face;
        }
        return SpecialType.NoSpecial;
    }

    private static boolean txtInTxt(Resources resources, String speakTxt, int res) {
        if (speakTxt.endsWith("。")) {
            speakTxt = speakTxt.substring(0, speakTxt.length() - 1);
        }
        return resources.getString(res).equals(speakTxt);
    }

    private static boolean txtInArray(String speakTxt, String[] speakArray) {
        if (speakTxt.endsWith("。")) {
            speakTxt = speakTxt.substring(0, speakTxt.length() - 1);
        }
        return Arrays.asList(speakArray).contains(speakTxt);
    }

    private static String[] resFoFinal(Resources resources, int id) {
        String[] res = resources.getStringArray(id);
        return res;
    }

    public static SpecialType doesExistLocal(Resources resources, String speakTxt) {
        if (txtInTxt(resources, speakTxt, R.string.Forward)) {
            return SpecialType.Forward;
        } else if (txtInTxt(resources, speakTxt, R.string.Backoff)) {
            return SpecialType.Backoff;
        } else if (txtInTxt(resources, speakTxt, R.string.Turnleft)) {
            return SpecialType.Turnleft;
        } else if (txtInTxt(resources, speakTxt, R.string.Turnright)) {
            return SpecialType.Turnright;
        } else if (txtInTxt(resources, speakTxt, R.string.Logout)) {
            return SpecialType.Logout;
        } else if (txtInTxt(resources, speakTxt, R.string.Map)) {
            return SpecialType.Map;
        } else if (txtInTxt(resources, speakTxt, R.string.StopListener)) {
            return SpecialType.StopListener;
        } else if (txtInTxt(resources, speakTxt, R.string.Back)) {
            return SpecialType.Back;
        } else if (txtInTxt(resources, speakTxt, R.string.Artificial)) {
            return SpecialType.Artificial;
        } else if (txtInTxt(resources, speakTxt, R.string.Face_check_in)) {
            return SpecialType.Face_check_in;
        } else if (txtInTxt(resources, speakTxt, R.string.Instagram)) {
            return SpecialType.Instagram;
        } else if (txtInTxt(resources, speakTxt, R.string.Witness_contrast)) {
            return SpecialType.Witness_contrast;
        } else if (txtInTxt(resources, speakTxt, R.string.Face_lifting_area)) {
            return SpecialType.Face_lifting_area;
        } else if (txtInTxt(resources, speakTxt, R.string.Next)) {
            return SpecialType.Next;
        } else if (txtInTxt(resources, speakTxt, R.string.Lase)) {
            return SpecialType.Lase;
        }
        return SpecialType.NoSpecial;
    }
}