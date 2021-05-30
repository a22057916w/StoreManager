<?php
$DBNAME = "storemanager";
$DBUSER = "root";
$DBPASSWD = "";
$DBHOST = "localhost";

$price = (int)$_POST['price'];//android將會傳值到number
$area = (int)$_POST['area'];
//$score = (int)$_POST['score'];

$conn = mysqli_connect( $DBHOST, $DBUSER, $DBPASSWD);
if (empty($conn)){
  print mysqli_error($conn);
  die ("無法連結資料庫");
  exit;
}
if( !mysqli_select_db($conn, $DBNAME)) {
  die ("無法選擇資料庫");
}

//$price1 = $price+10000;
// 設定連線編碼
mysqli_query( $conn, "SET NAMES utf8");
$sql ="select post_id, addr, area, price, avg_score  from `sells_total_rows_ntc` NATURAL JOIN `sells_score_ntc`
where price >= $price AND price <= ($price+10) AND area >= $area AND area <= ($area+10)
order by avg_score DESC";

/*select post_id, addr, area, price, avg_score  from `sells_total_rows_tpe` NATURAL JOIN `sells_score_tpe`
where price >= $price AND area >= $area 
order by avg_score DESC"*/

$result = mysqli_query($conn, $sql);
//$response = array();
if($result)
	{
		
		//$row = mysqli_fetch_array($result);
		//print $row[1];
		
		while($row = mysqli_fetch_array($result))
		{
			$flag[] = $row;
			//array_push($response,array("name"=>$row[0],"people"=>$row[1],"avg"=>$row[2]));
			
 		}
		print(json_encode($flag,JSON_UNESCAPED_UNICODE));
		//echo json_encode(array(""=>$response),JSON_UNESCAPED_UNICODE);
	}
?>
