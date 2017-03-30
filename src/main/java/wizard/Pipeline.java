package wizard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by natha on 2/25/2017.
 */
public class Pipeline
{

    private String language;
    private List<String> stages = new ArrayList<String>();

    public Pipeline(String language, List<String> stages)
    {

        this.language = language;
        this.stages = stages;

    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getLanguage()
    {
        return this.language;
    }

    public List<String> getStages()
    {
        return stages;
    }

    public void setStage(String stage)
    {
        this.stages.add(stage);
    }

}
