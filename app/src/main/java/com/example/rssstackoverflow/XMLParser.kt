package com.example.rssstackoverflow
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.URL

/*
    <entry>
        <id>https://stackoverflow.com/q/70361971</id>
        <re:rank scheme="https://stackoverflow.com">0</re:rank>
        <title type="text">&quot;share&quot; a custom task pane between all windows of the same presentation</title>
            <category scheme="https://stackoverflow.com/tags" term="excel" />
            <category scheme="https://stackoverflow.com/tags" term="powerpoint" />
            <category scheme="https://stackoverflow.com/tags" term="vsto" />
        <author>
            <name>chriscode</name>
            <uri>https://stackoverflow.com/users/14832932</uri>
        </author>
        <link rel="alternate" href="https://stackoverflow.com/questions/70361971/share-a-custom-task-pane-between-all-windows-of-the-same-presentation" />
        <published>2021-12-15T10:20:08Z</published>
        <updated>2021-12-15T10:20:08Z</updated>
        <summary type="html">
            &lt;p&gt;I use the below code to manage different custom task panes in PowerPoint VSTO across presentations. This works fine, e.g. when a user opens a new presentation a new task pane is created and it does not affect any other open presentation task panes.&lt;/p&gt;&#xA;&lt;p&gt;Now I encountered the following situation. A user has opened a presentation and now opens an additional window in PowerPoint for this presentation (click &amp;quot;View&amp;quot;, &amp;quot;New window&amp;quot;). Now what happens is that a new custom task pane (because this window&#x27;s HWND is different) is created but instead I need this task pane to be the same as in the other presentation window.&lt;/p&gt;&#xA;&lt;p&gt;&lt;strong&gt;Question: how can I &amp;quot;share&amp;quot; a task pane between all windows of the same presentation?&lt;/strong&gt;&lt;/p&gt;&#xA;&lt;pre&gt;&lt;code&gt;Dim CreatedPanes As New Dictionary(Of String, CustomTaskPane)&#xA;&#xA;Public Function GetTaskPane(taskPaneId As String, taskPaneTitle As String) As Microsoft.Office.Tools.CustomTaskPane&#xA;&#xA;    Dim key As String = $&amp;quot;{taskPaneId}({Globals.ThisAddIn.Application.HWND})&amp;quot;&#xA;&#xA;    If Not CreatedPanes.ContainsKey(key) Then&#xA;        Dim pane = Globals.ThisAddIn.CustomTaskPanes.Add(New myTaskPaneControl(), taskPaneTitle)&#xA;&#xA;&#xA;        CreatedPanes(key) = pane&#xA;&#xA;    End If&#xA;&#xA;    Return CreatedPanes(key)&#xA;&#xA;End Function&#xA;&lt;/code&gt;&lt;/pre&gt;&#xA;&lt;p&gt;I think the same logic will apply to Excel as well, hence I am sldo adding this tag to the question.&lt;/p&gt;&#xA;
        </summary>
    </entry>
*/

class XMLParser {
    private val questions = ArrayList<Question>()
    private var text = ""

    private var title = ""
    private val category = mutableListOf<String>()

    private var author = ""
    private var published = ""
    private var summary = ""

    fun parse(): ArrayList<Question>{
        try{
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            val url = URL("https://stackoverflow.com/feeds")
            parser.setInput(url.openStream(), null)
            var eventType = parser.eventType
            while(eventType != XmlPullParser.END_DOCUMENT){
                val tagName = parser.name
                when(eventType){
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.START_TAG -> when {
                        tagName.equals("category", true) -> category.add(parser.getAttributeValue(1))
                        else -> {}
                    }
                    XmlPullParser.END_TAG -> when {
                        tagName.equals("title", true) -> title = text
                        tagName.equals("name", true) -> author = text
                        tagName.equals("published", true) -> published = text
                        tagName.equals("summary", true) -> {
                            summary = text


                            var categories = mutableListOf<String>()
                            for (c in category) {
                                categories.add(c)
                            }

                            questions.add(Question(title, categories, author, published, summary))
                            category.clear()
                        }
                        else -> {}
                    }
                    else -> {}
                }
                eventType = parser.next()
            }
        }catch(e: XmlPullParserException){
            e.printStackTrace()
        }catch(e: IOException){
            e.printStackTrace()
        }
        return questions
    }
}