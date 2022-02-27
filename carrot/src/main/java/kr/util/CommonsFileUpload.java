package kr.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CommonsFileUpload {
	public static final String ENCODING_TYPE = "UTF-8"; // 파라미터 인코딩 타입
	public static final int MAX_MEMORY_SIZE = 1024*1024; // 메모리 저장 한계 크기
	public static final int MAX_FILE_SIZE = 1024*1024*5; // 개별 파일의 최대 크기
	public static final String UPLOAD_PATH = "/upload"; // 업로드 상대 경로
	public static final String TEMP_PATH = "/WEB-INF/temp"; // 임시 파일 저장소 상대 경로

	public static Map<String, String> uploadFile(HttpServletRequest request) throws Exception {
		Map<String, String> multi = new HashMap<String, String>();
		
		String realPath = request.getServletContext().getRealPath(UPLOAD_PATH); // 업로드 절대 경로 구하기
		String tempPath = request.getServletContext().getRealPath(TEMP_PATH); // 임시 파일 저장소 절대 경로 구하기
		
		int count = 0; // 업로드된 파일의 수
		
		// FileItem의 크기에 따른 관리를 위해 DiskFileItemFactory 생성
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MAX_MEMORY_SIZE); // 지정한 값보다 크기가 큰 파일 업로드시 메모리에 있는 바이너리 데이터를 임시 파일 저장소에 임시 파일로 저장
		factory.setRepository(new File(tempPath));
		
		// 업로드 요청을 처리하는 ServletFileUpload 생성
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE); // 지정한 값보다 크기가 큰 파일은 업로드 불가
		
		// request에서 multipart/form-data 형식으로 넘어온 데이터 처리
		List<FileItem> inputs = upload.parseRequest(request);
		for(FileItem input : inputs) { // FileItem에는 파라미터 혹은 파일 정보가 담겨 있음
			if(input.isFormField()) { // 현재 FileItem이 파라미터이면
				multi.put(input.getFieldName(), input.getString(ENCODING_TYPE));
			}
			else { // 현재 FileItem이 파일이면
				count++; // 업로드된 파일의 수 증가
				if(input.getSize()>0) {
					File file = renameFile(new File(realPath + File.separator + input.getName()));
					input.write(file); // 파일을 업로드
					input.delete(); // 임시 파일이 생성되었으면 제거
					
					multi.put(input.getFieldName() + count, file.getName());
				}
			}
		}
		
		return multi;
	}
	
	// 중복 파일명 처리
	public static File renameFile(File file) {
		if(file.exists()) { // 파일이 존재하면
			String fileName = file.getName(); // 경로를 제외한 실제 파일명
			String baseName = null; // 실제 파일명에서 확장자 전까지
			String extension = null;// 확장자명
			int period = fileName.lastIndexOf(".");		
			if(period!=-1) { // 확장자가 있으면
				baseName = fileName.substring(0, period);	
				extension = fileName.substring(period);
			}
			else { // 확장자가 없으면
				baseName = fileName;
				extension = "";
			}
			
			int count = 1;
			while(file.exists()) { // 파일이 존재하는 동안
				file = new File(file.getParent(), baseName + count + extension); // 기존 파일명 다음에 숫자를 붙임
				count++;
			}
		}
		
		return file;
	}
	
	// 파일 삭제
	public static void removeFile(HttpServletRequest request, String fileName) {
		if(fileName!=null) {
			String realPath = request.getServletContext().getRealPath(UPLOAD_PATH); // 업로드 절대 경로 구하기
			File file = new File(realPath + File.separator + fileName);
			if(file.exists()) file.delete();
		}
	}
}