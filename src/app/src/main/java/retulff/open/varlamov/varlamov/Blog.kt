package retulff.open.varlamov.varlamov

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.joda.time.DateTime
import retrofit2.Callback
import retulff.open.varlamov.varlamov.platform.livejournal.service.LiveJournalService

class Blog {
    companion object {

        private const val ljName = "varlamov.ru"
        private var ljService: LiveJournalService = LiveJournalService.create()

        private fun execute(requestXml: String, callback: Callback<ResponseBody>) {

            val requestBody = RequestBody.create(MediaType.parse("text/xml"), requestXml)
            val ljMethod = ljService.executeMethod(requestBody)

            ljMethod.enqueue(callback)
        }

        fun getPublications(
            items: Int,
            beforeDate: DateTime,
            callback: Callback<ResponseBody>
        ) {

            val requestXml = buildPublicationsRequestXml(items, beforeDate)
            execute(requestXml, callback)
        }

        private fun buildPublicationsRequestXml(
            itemShow: Int,
            beforeDate: DateTime
        ): String {

            val beforeDateFormatted = beforeDate.toString("yyyy-MM-dd HH:mm:ss")

            return """
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
							<string>$ljName</string>
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
							<i4>$itemShow</i4>
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
        """.trim()
        }

        fun getPublicationsByTag(
            tag: String,
            items: Int,
            beforeDate: DateTime,
            callback: Callback<ResponseBody>
        ) {

            val requestXml = buildByTagRequestXml(tag, items, beforeDate)
            execute(requestXml, callback)
        }

        private fun buildByTagRequestXml(tag: String, itemShow: Int, beforeDate: DateTime): String {

            val beforeDateFormatted = beforeDate.toString("yyyy-MM-dd HH:mm:ss")

            return """
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
                     <string>$ljName</string>
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
                     <i4>$itemShow</i4>
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
        """.trim()
        }

        fun getPublicationsOfDay(
            items: Int,
            day: DateTime,
            callback: Callback<ResponseBody>
        ) {

            val requestXml = buildOfDayRequestXml(items, day)
            execute(requestXml, callback)
        }

        private fun buildOfDayRequestXml(itemShow: Int, day: DateTime): String {
            return """
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
                            <string>$ljName</string>
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
                            <i4>$itemShow</i4>
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
</methodCall>
        """.trim()
        }
    }
}