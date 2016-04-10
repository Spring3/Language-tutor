package tutor.tasks.dictation;

import tutor.models.Language;

/**
 * Created by Spring on 4/10/2016.
 */
public class AbstractDictation {

    public AbstractDictation(Dictation.DictationType dictationType, Language langToLearn){
        this.dictationType = dictationType;
        this.language = langToLearn;
    }

    public AbstractDictation(Dictation.DictationType dictationType, Dictation.Mode mode, Language langToLearn){
        this.dictationType = dictationType;
        this.mode = mode;
        this.language = langToLearn;
    }

    private Dictation.DictationType dictationType;
    private Dictation.Mode mode;
    private Language language;

    public Dictation.DictationType getDictationType(){
        return dictationType;
    }

    public Dictation.Mode getDictationMode(){
        return mode;
    }

    public Language getLanguage(){
        return language;
    }
}
