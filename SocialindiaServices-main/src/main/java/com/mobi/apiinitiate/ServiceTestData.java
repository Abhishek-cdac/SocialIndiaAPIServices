package com.mobi.apiinitiate;

public class ServiceTestData {
	
	// A - Action
		// D - Data

		public static String devicesA = "deviceinfo";
		public static String devicesD = "{\"servicecode\" : \"0002\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\" : {\"deviceinfo\" : {\"devicekey\" : \"111111\",\"imeino\" : \"11111\",\"deviceid\" : \"111111\",\"macaddr\" : \"11111\",\"serialno\" : \"111111\",\"androidversion\" : \"11111\",\"displayinfo\":\"1111\",\"ipaddress\":\"11111\",\"latlogitude\":\"111111\",\"additionalinfoone\" : \"\",\"additionalinfotwo\" : \"\"},\"isfirst\" : \"yes\",\"profile_mobile\" : \"9940404040\"}}";

		public static String reoprtA = "reportissue";
		public static String reoprtD = "{\"servicecode\" : \"0006\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"emailid\":\"joss@ss.ss\",\"mobileno\" : \"9551500022\",\"name\" : \"joss\",\"issuedesc\" : \"thisd ksdf n sjdfkds\",\"pp_resident\":\"\"}}";

		public static String contactusA = "contactus";
		public static String contactusD = "{\"servicecode\" : \"0006\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"emailid\":\"suresh@ss.ss\",\"mobileno\" : \"9551500099\",\"name\" : \"suresh\",\"desc\" : \"thisd ksdf n sjdfkds\"}}";

		public static String skilldirectoryA = "skilled_directory_search";
		public static String skilldirectoryD = "{\"servicecode\":\"0059\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"timestamp\":\"\",\"startlimit\":\"0\",\"skill_category\":\"\",\"year_exp\":\"\",\"rating\":\"\",\"locaton\":\"\"}}";

		public static String bookingInsertA = "service_booking_new";
		public static String bookingInsertD = "{\"servicecode\":\"0023\",\"townshipid\":\"SI8WDA0106OX1ONO9KOH106OX\",\"societykey\": \"SIK8WDA0106OX1ONO9KOH106OX\",\"data\":{\"rid\":\"1\",\"labor_id\":\"2\",\"problem\":\"text\",\"prefered_datetime\":\"2016-10-10 10:00 - 14:00\"}}";

		public static String bookingUpdateA = "service_booking_update";
		public static String bookingUpdateD = "{\"servicecode\":\"0024\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\": \"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"booking_id\":\"2\",\"labor_id\":\"1\",\"status\":\"2\",\"start_datetime\":\"2016-10-10 09:30:23\",\"end_datetime\":\"2016-10-10 09:30:23\",\"service_cost\":\"865.00\",\"rating\":\"1\",\"feedback\":\"text again\"}}";

		public static String bookingDeleteA = "service_booking_delete";
		public static String bookingDeleteD = "{\"servicecode\":\"0024\",\"townshipid\":\"SI8WDA0106OX1ONO9KOH106OX\",\"societykey\": \"SIK8WDA0106OX1ONO9KOH106OX\",\"data\":{\"rid\":\"1\",\"booking_id\":\"1\",\"status\":\"1\"}}";

		public static String bookingListA = "my_service_booking_list";
		public static String bookingListD = "{\"servicecode\":\"0024\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\": \"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"timestamp\":\"\",\"startlimit\":\"\"}}";
		// Feed
		public static String feedCommentPostA = "comment_post";
		public static String feedCommentPostD = "{\"servicecode\":\"0030\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\": \"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"49\",\"feed_id\":\"2\",\"comment\":\"text list\"}}";

		public static String feedCommentListA = "comment_list";
		public static String feedCommentListD = "{\"servicecode\":\"0029\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\": \"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"49\",\"feed_id\":\"1\",\"startlimit\":\"\",\"timestamp\":\"\"}}";

		public static String feedCommentDeleteA = "comment_delete";
		public static String feedCommentDeleteD = "{\"servicecode\":\"0028\",\"townshipid\":\"SI8WDA0106OX1ONO9KOH106OX\",\"societykey\": \"SIK8WDA0106OX1ONO9KOH106OX\",\"data\":{\"rid\":\"54\",\"feed_id\":\"1\",\"comment_id\":\"1\"}}";

		public static String feedLikeUnlikeA = "feed_like_unlike";
		public static String feedLikeUnlikeD = "{\"servicecode\":\"0027\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\": \"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"feed_id\":\"223\",\"like_sts\":\"1\"}}";

		public static String feedListA = "feed_list";
//		public static String feedListD = "{\"servicecode\":\"0026\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"timestamp\":\"\",\"startlimit\":\"0\",\"feed_type\":\"2\",\"feed_category\":\"\",\"post_by\":\"\"}}";
		public static String feedListD = "{\"servicecode\":\"0026\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\":\"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"timestamp\":\"\",\"startlimit\":\"10\",\"feed_type\":\"\",\"feed_category\":\"\",\"post_by\":\"\"}}";

		public static String feedPostA = "feed_post";
		public static String feedPostD = "{\"servicecode\":\"0032\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"feed_id\":\"9\",\"feed_msg\":\"feed sdsd\",\"privacy\":\"1\",\"users\":[],\"remove_attach\":[],\"urls\":[]}}";
//		public static String feedPostD = "{\"servicecode\":\"0032\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\":\"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"feed_id\":\"\",\"feed_view_id\":\"\",\"feed_msg\":\"bxbzProcNew\",\"privacy\":\"3\",\"users\":[],\"urls\":[],\"remove_attach\":[]}}";
		
		public static String feedEditA = "feed_edit";
		public static String feedEditD = "{\"servicecode\":\"0032\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"feed_id\":\"7\",\"rid\":\"49\",\"feed_msg\":\"text editt \",\"privacy\":\"2\",\"users\":\"3,4,77,98\",\"total_size\":\"3\",\"urls\":[{\"thumb_img\":\"http://hhs/jjeditt\",\"title\":\"wwwwweditt\",\"pageurl\":\"http://hhs/jjeditt\"},{\"thumb_img\":\"http://hhs/jj\",\"title\":\"wwwww\",\"pageurl\":\"http://hhs/jj\"}]}}";

		public static String feedShareA = "feed_share";
		public static String feedShareD = "{\"servicecode\":\"0032\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\":\"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"feed_id\":\"217\",\"feed_view_id\":\"251\",\"privacy\":\"4\",\"users\":[48,52,884,890]}}";
		
		public static String feedDeleteA = "feed_delete";
		public static String feedDeleteD = "{\"servicecode\":\"0032\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\":\"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"feed_id\":\"218\",\"feed_view_id\":\"266\",\"feed_del_flg\":\"1\"}}";
		
		
		public static String feedPrivacyEditA = "feed_privacy_update";
		public static String feedPrivacyEditD = "{\"servicecode\":\"0032\",\"townshipid\":\"6ZVG6UNCR4OR9TAFUPEFL6W2O\",\"societykey\":\"L5NUFFK9FDID4OIH2U5ESJ2YT\",\"data\":{\"rid\":\"902\",\"feed_flg\":\"2\",\"feed_id\":\"146\",\"feed_view_id\":\"153\",\"privacy\":\"3\",\"users\":[]}}";
		
		public static String privacyCategoryA = "privacy_category";
		public static String privacyCategoryD = "{\"servicecode\":\"0033\",\"townshipid\":\"SI8WDA0106OX1ONO9KOH106OX\",\"societykey\": \"SIK8WDA0106OX1ONO9KOH106OX\",\"data\":{\"rid\":\"48\"}}";

		public static String privacyUsersA = "privacy_users";
		public static String privacyUsersD = "{\"servicecode\":\"0034\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"31\",\"cate_type\":\"4\",\"timestamp\":\"\",\"startlimit\":\"\",\"search_text\":\"eeeee\"}}";

		public static String privacyUpdateA = "privacy_update";
		public static String privacyUpdateD = "{\"servicecode\":\"0034\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"privacy\":\"4\",\"users\":[5,6]}}";

		public static String userDetailsA = "";
		public static String userDetailsD = "";
		// Timeline Message
		public static String sendMsgA = "send_message";
		public static String sendMsgD = "{\"servicecode\":\"0034\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"group_contact\":\"2\",\"group_contact_id\":\"1\",\"msgtype\":\"1\",\"content\":\"hi again\"}}";

		public static String createGroupA = "create_rename_group";
		public static String createGroupD = "{\"servicecode\":\"0034\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"49\",\"group_name\":\"Ben10\",\"group_id\":\"4\",\"member_id\":\"\"}}";

		public static String deleteGroupA = "delete_group";
		public static String deleteGroupD = "{\"servicecode\":\"0034\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"49\",\"group_id\":\"4\"}}";

		public static String updateGrpA = "updateGroupInfo";
		public static String updateGrpD = "{\"servicecode\":\"0034\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"49\",\"group_id\":\"2\",\"group_name\":\"Killer\",\"group_image\":\"IMG.JPG\"}}";

		public static String addRemoveGrpUsrA = "update_group_user";
		public static String addRemoveGrpUsrD = "{\"servicecode\":\"0034\",\"townshipid\":\"O7PW0SUUML91G3N48LXQ75NTU\",\"societykey\":\"KARKZD0LPTFRHMO4JD26DE9RB\",\"data\":{\"rid\":\"49\",\"group_id\":\"4\",\"update_type\":\"1\",\"member_id\":\"[160]\"}}";

		public static String listGrpMsgA = "search_group_contact";
		public static String listGrpMsgD = "{\"servicecode\":\"0037\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"group_contact\":\"1\",\"search_text\":\"h\",\"timestamp\":\"1222223456\",\"startlimit\":\"0\"}}";

		public static String listMsgA = "chat_history";
		public static String listMsgD = "{\"servicecode\":\"0037\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"group_contact\":\"1\",\"group_contact_id\":\"48\",\"timestamp\":\"1222223456\",\"startlimit\":\"0\"}}";

		public static String skillpublichAddEditA = "publish_add_edit";
		public static String skillpublichAddEditD = "{\"servicecode\":\"0032\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"feed_id\":\"140\",\"add_edit\":\"2\",\"publish_skill_id\":\"11\",\"category_id\":\"2\",\"skill_id\":\"25\",\"title\":\"3D Animation EDIT AG\",\"duration\":\"5\",\"duration_flg\":\"1\",\"avbil_days\":[5,6],\"brief_desc\":\"INFO XmlConfigurationProvider - Parsing configuration file \",\"fees\":\"2500\"}}";
		
		public static String skillpublichSearchA = "search_list";
		public static String skillpublichSearchD = "{\"servicecode\":\"0026\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"timestamp\":\"\",\"startlimit\":\"\",\"search_flg\":\"1\",\"category_id\":\"\",\"skill_id\":\"\",\"title\":\"\"}}";
		
		public static String skillpublichDelA = "publish_delete";
		public static String skillpublichDelD = "{\"servicecode\":\"0034\",\"townshipid\":\"O4TMCW5UGVK2LAAPLJMLG6O7J\",\"societykey\":\"YPKHMJSZ80K2T2ZWZB87CPG1L\",\"data\":{\"rid\":\"49\",\"group_id\":\"4\"}}";
		
		public static String testA = "";
		public static String testD = "";

}
