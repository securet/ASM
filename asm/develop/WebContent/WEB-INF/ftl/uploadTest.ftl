<html>
<body>
	<form method="POST" enctype="multipart/form-data"
		action="/asm/upload?${_csrf.parameterName}=${_csrf.token}">
		File to upload <input type="file" name="file"><br />: Name: <input
			type="text" name="name"><br /> <br /> <input type="submit"
			value="Upload"> Press here to upload the file!	</form>
</body>
</html>