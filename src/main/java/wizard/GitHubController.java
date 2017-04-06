package wizard;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by natha on 3/29/2017.
 */
public class GitHubController
{

    private String username;
    private String personalToken;

    private int numOfRepositories = 10;
    private String webhookURL;
    private static ArrayList<GHRepository> repositories = new ArrayList<GHRepository>();
    private GitHub github;
    private GHOrganization organization;
    private String orgName;

    public GitHubController(String username, String personalToken, String orgName, int numOfRepositories, String webhookURL) throws Exception
    {

        this.username = username;
        this.personalToken = personalToken;

        this.github = GitHub.connect(username, personalToken);
        this.orgName = orgName;
        this.organization = github.getOrganization(orgName);
        this.numOfRepositories = numOfRepositories;
        this.webhookURL = webhookURL;

        github.checkApiUrlValidity();

    }

    /*public static void main(String[] args)
    {

        try
        {
            /*
            // Set up the credentials for the API to use using AuthO token with github
            github = GitHub.connect("nathanacooper", "16e242ed7cbe49bd667b245eb7a1490f45a44054");
            org = github.getOrganization("DevOps-Pipeline");
            DevopsConf.createRepos();
            DevopsConf.setUpRepositories();
            DevopsConf.setUpTeams();
            //HelloWorld.removeRepos();
            //HelloWorld.removeTeams();
            //GHCreateRepositoryBuilder builder = org.createRepository("Test5");
            //builder.create();
            // GHCreateRepositoryBuilder repo = github.createRepository("TestRepo3");
            //GHRepository repo = org.getRepository("Test5");
            //GHTeam team = org.createTeam("Team3", GHOrganization.Permission.PULL);
            //team.add(github.getUser("nathanacooper"));
            //team.add(repo);
            github.checkApiUrlValidity();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Hello, World!");
    }*/

    /**
     * Creates github repositories under a specific organization
     * @throws IOException
     */
    public void createRepos() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHCreateRepositoryBuilder builder = this.organization.createRepository("Repo" + i);
            repositories.add(builder.create());
            System.out.println("Creating repo...");

        }

    }

    /**
     * Populates each repository with the PHP application
     * @throws GitAPIException
     * @throws IOException
     */
    public void setUpRepositories(String path) throws GitAPIException, IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            Git git = Git.open(new File(path));
            git.push()
                    .setCredentialsProvider( new UsernamePasswordCredentialsProvider(this.username, this.personalToken))
                    .setRemote(String.format("https://github.com/%s/Repo%d.git", this.orgName, i))
                    .setPushAll()
                    .call();
            System.out.println("Setting up repo...");

        }

    }

    /**
     * Creates a webhook for each repository
     * https://developer.github.com/v3/repos/hooks/#create-a-hook
     * @throws GitAPIException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public void createWebhook() throws GitAPIException, IOException
    {

        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, personalToken.toCharArray());
            }
        });

        HttpURLConnection con = (HttpURLConnection) new URL(String.format("https://api.github.com/orgs/%s/hooks", this.orgName)).openConnection();
        String userPassword = this.username + ":" + this.personalToken;
        String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());

        con.setRequestProperty("Authorization", "Basic " + encoding);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.getOutputStream().write(String.format("{"
                                                + "`name`: `web`,"
                                                + "`active`: true,"
                                                + "`events`: [`push`],"
                                                + "`config`: {"
                                                + "`url`: `%s`"
                                                + "}", this.webhookURL).replace('`', '"').getBytes("UTF-8"));
        con.getInputStream();

    }

    /**
     * Deletes github repositories under a specific organization
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public void removeRepos() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHRepository repo = this.organization.getRepository("Repo" + i);
            repo.delete();
            System.out.println("Deleting repo...");

        }

        repositories.clear();

    }

    /**
     * Creates and populates the team with members and assigns the team a repository
     * @throws IOException
     */
    public void setUpTeams() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHTeam team = this.organization.createTeam("Team" + i, GHOrganization.Permission.PULL);
            team.add(github.getUser(this.username));
            team.add(repositories.get(i - 1));
            System.out.println("Setting up team...");

        }

    }

    /**
     * Deletes teams
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public void removeTeams() throws IOException
    {

        for (int i = 1; i <= this.numOfRepositories; i++)
        {

            GHTeam team = this.organization.getTeamByName("Team" + i);
            team.delete();
            System.out.println("Deleting team...");

        }

    }

}
