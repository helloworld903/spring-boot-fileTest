package com.test.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {
	private String filePath="D:\\";
	
	@GetMapping("/downloadFile/{file}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("file") String fileName,HttpServletResponse response) throws IOException {
		File file=new File(filePath+fileName);
		if(!file.exists())return ResponseEntity.notFound().build();
		
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=test"); //以附件形式下载
		
		byte[]buff=new byte[1024];
		FileInputStream fileInputStream = new FileInputStream(file);
		BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
		OutputStream outputStream=response.getOutputStream();
		
		int bytesRead=-1;
		while((bytesRead=bufferedInputStream.read(buff))!=-1) {
			outputStream.write(buff,0,buff.length);
			outputStream.flush();
		}
		outputStream.flush();
		outputStream.close();
		bufferedInputStream.close();
		fileInputStream.close();
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/previewFile/{file}")
	public ResponseEntity<Resource> previewFile(@PathVariable("file") String fileName,HttpServletResponse response ) throws IOException {
		File file=new File(filePath+fileName);
		if(!file.exists())return ResponseEntity.notFound().build();
				
				
		String fileType = Files.probeContentType(file.toPath());
		FileInputStream fileInputStream=new FileInputStream(file);
		
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType(fileType);
		OutputStream outputStream = response.getOutputStream();
		
		int count=0;
		byte[]buffer=new byte[1024*1024];
		while((count=fileInputStream.read(buffer))!=-1) {
			outputStream.write(buffer,0,count);
		}
		outputStream.flush();
		outputStream.close();
		fileInputStream.close();
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file) throws IOException{
		//获取文件名和文件内容类型
		String fileName=file.getOriginalFilename();
		String contentType=file.getContentType();
		//long size=file.getSize();
		
		//打印文件信息
        System.out.println("Received file: " + fileName);
        System.out.println("Content type: " + contentType);
		//保存到磁盘
		File newFile=new File(filePath+fileName);
		file.transferTo(newFile);
		
		return  ResponseEntity.ok("file upload successfully");
	}
}
