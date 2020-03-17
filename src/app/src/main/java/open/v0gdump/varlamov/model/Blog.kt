package open.v0gdump.varlamov.model

import okhttp3.ResponseBody
import open.v0gdump.varlamov.extension.optimize
import open.v0gdump.varlamov.model.platform.livejournal.service.LiveJournalService
import org.joda.time.DateTime
import retrofit2.Callback

object Blog {

    private const val journalName = "varlamov.ru"

    private val ljService = LiveJournalService.create()

    private fun execute(requestXml: String, callback: Callback<ResponseBody>) =
        ljService.executeMethod(requestXml).enqueue(callback)

    fun getPublications(
        count: Int,
        before: DateTime,
        callback: Callback<ResponseBody>
    ) {
        val beforeDateFormatted = before.toString("yyyy-MM-dd HH:mm:ss")

        execute(
            requestXml = """
<?xml version="1.0"?>
<methodCall>
	<methodName>LJ.XMLRPC.getevents</methodName>
	<params>
		<param>
			<value>
				<struct>
					<member>
						<name>ver</name>
						<value>
							<i4>4</i4>
						</value>
					</member>
					<member>
						<name>get_video_ids</name>
						<value>
							<boolean>0</boolean>
						</value>
					</member>
					<member>
						<name>get_users_info</name>
						<value>
							<boolean>0</boolean>
						</value>
					</member>
					<member>
						<name>journal</name>
						<value>
							<string>$journalName</string>
						</value>
					</member>
					<member>
						<name>get_polls</name>
						<value>
							<boolean>0</boolean>
						</value>
					</member>
					<member>
						<name>selecttype</name>
						<value>
							<string>before</string>
						</value>
					</member>
					<member>
						<name>itemshow</name>
						<value>
							<i4>$count</i4>
						</value>
					</member>
					<member>
						<name>before</name>
						<value>
							<string>$beforeDateFormatted</string>
						</value>
					</member>
					<member>
						<name>sort_order</name>
						<value>
							<string>desc</string>
						</value>
					</member>
				</struct>
			</value>
		</param>
	</params>
</methodCall>
        """.optimize(),
            callback = callback
        )
    }

    fun getPublicationsByTag(
        tag: String,
        items: Int,
        beforeDate: DateTime,
        callback: Callback<ResponseBody>
    ) {
        val beforeDateFormatted = beforeDate.toString("yyyy-MM-dd HH:mm:ss")

        execute(
            requestXml = """
<?xml version="1.0" encoding="UTF-8"?>
<methodCall>
   <methodName>LJ.XMLRPC.getevents</methodName>
   <params>
      <param>
         <value>
            <struct>
               <member>
                  <name>ver</name>
                  <value>
                     <i4>4</i4>
                  </value>
               </member>
               <member>
                  <name>get_video_ids</name>
                  <value>
                     <boolean>1</boolean>
                  </value>
               </member>
               <member>
                  <name>get_users_info</name>
                  <value>
                     <boolean>1</boolean>
                  </value>
               </member>
               <member>
                  <name>journal</name>
                  <value>
                     <string>$journalName</string>
                  </value>
               </member>
               <member>
                  <name>get_polls</name>
                  <value>
                     <boolean>1</boolean>
                  </value>
               </member>
               <member>
                  <name>tags</name>
                  <value>
                     <string>$tag</string>
                  </value>
               </member>
               <member>
                  <name>selecttype</name>
                  <value>
                     <string>tag</string>
                  </value>
               </member>
               <member>
                  <name>itemshow</name>
                  <value>
                     <i4>$items</i4>
                  </value>
               </member>
               <member>
                  <name>beforedate</name>
                  <value>
                     <string>$beforeDateFormatted</string>
                  </value>
               </member>
            </struct>
         </value>
      </param>
   </params>
</methodCall>
        """.optimize(),
            callback = callback
        )
    }

    fun getPublicationsOfDay(
        items: Int,
        day: DateTime,
        callback: Callback<ResponseBody>
    ) {
        execute(
            requestXml = """<?xml version="1.0"?>
<methodCall>
    <methodName>LJ.XMLRPC.getevents</methodName>
    <params>
        <param>
            <value>
                <struct>
                    <member>
                        <name>ver</name>
                        <value>
                            <i4>4</i4>
                        </value>
                    </member>
                    <member>
                        <name>get_video_ids</name>
                        <value>
                            <boolean>0</boolean>
                        </value>
                    </member>
                    <member>
                        <name>get_users_info</name>
                        <value>
                            <boolean>0</boolean>
                        </value>
                    </member>
                    <member>
                        <name>journal</name>
                        <value>
                            <string>$journalName</string>
                        </value>
                    </member>
                    <member>
                        <name>get_polls</name>
                        <value>
                            <boolean>0</boolean>
                        </value>
                    </member>
                    <member>
                        <name>selecttype</name>
                        <value>
                            <string>day</string>
                        </value>
                    </member>
                    <member>
                        <name>itemshow</name>
                        <value>
                            <i4>$items</i4>
                        </value>
                    </member>
                    <member>
                        <name>year</name>
                        <value>
                            <int>${day.year}</int>
                        </value>
                    </member>
                    <member>
                        <name>month</name>
                        <value>
                            <int>${day.monthOfYear}</int>
                        </value>
                    </member>
                    <member>
                        <name>day</name>
                        <value>
                            <int>${day.dayOfMonth}</int>
                        </value>
                    </member>
                    <member>
                        <name>sort_order</name>
                        <value>
                            <string>desc</string>
                        </value>
                    </member>
                </struct>
            </value>
        </param>
    </params>
</methodCall>""".optimize(),
            callback = callback
        )
    }
}