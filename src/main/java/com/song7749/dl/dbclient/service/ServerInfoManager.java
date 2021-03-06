package com.song7749.dl.dbclient.service;

import java.util.List;
import java.util.Map;

import com.song7749.dl.dbclient.dto.DeleteServerInfoDTO;
import com.song7749.dl.dbclient.dto.ExecuteResultListDTO;
import com.song7749.dl.dbclient.dto.FindServerInfoDTO;
import com.song7749.dl.dbclient.dto.FindServerInfoListDTO;
import com.song7749.dl.dbclient.dto.FindTableDTO;
import com.song7749.dl.dbclient.dto.ModifyServerInfoDTO;
import com.song7749.dl.dbclient.dto.SaveServerInfoDTO;
import com.song7749.dl.dbclient.vo.DatabaseDdlVO;
import com.song7749.dl.dbclient.vo.FieldVO;
import com.song7749.dl.dbclient.vo.FunctionVO;
import com.song7749.dl.dbclient.vo.IndexVO;
import com.song7749.dl.dbclient.vo.ProcedureVO;
import com.song7749.dl.dbclient.vo.SequenceVO;
import com.song7749.dl.dbclient.vo.ServerInfoVO;
import com.song7749.dl.dbclient.vo.TableVO;
import com.song7749.dl.dbclient.vo.TriggerVO;
import com.song7749.dl.dbclient.vo.ViewVO;
/**
 * <pre>
 * Class Name : ServerInfoManager.java
 * Description : 서버 정보 저장 매니저.
 * 서버 정보 저장 매니저를 통해 DBclient 에서 접속할 서버들의 정보를 저장한다.
 *
 *  Modification Information
 *  Modify Date 		Modifier	Comment
 * -----------------------------------------------
 *  2015. 4. 26.		MS Song		신규작성
 *
 * </pre>
 *
 * @author MS Song
 * @since 2015. 4. 26.
 */
public interface ServerInfoManager {

	/**
	 * 서버 정보를 저장한다.
	 * @param dto
	 */
	void saveServerInfo(SaveServerInfoDTO dto);

	/**
	 * 서버 정보를 수정한다.
	 * @param dto
	 */
	void modifyServerInfo(ModifyServerInfoDTO dto);

	/**
	 * 서버 정보를 삭제한다.
	 * @param dto
	 */
	void deleteServerInfo(DeleteServerInfoDTO dto);

	/**
	 * 서버 정보 조회
	 * @param dto
	 * @return ServerInfoVO
	 */
	ServerInfoVO findServerInfo(FindServerInfoDTO dto);

	/**
	 * 서버 정보 리스트를 조회
	 * @param dto
	 * @return List<ServerInfoVO>
	 */
	List<ServerInfoVO> findServerInfoList(FindServerInfoListDTO dto);

	/**
	 * 서버 정보 일괄 입력
	 * @param list
	 */
	void saveServerInfoFacade(List<SaveServerInfoDTO> list);

	/**
	 * 서버 정보 일괄 수정
	 * @param list
	 */
	void modifyServerInfoFacade(List<ModifyServerInfoDTO> list);

	/**
	 * 서버 정보 일괄 삭제
	 * @param list
	 */
	void deleteServerInfoFacade(List<DeleteServerInfoDTO> list);

	/**
	 * 테이블 정보 리스트
	 * @param dto
	 * @return List<TableVO>
	 */
	List<TableVO> findTableVOList(FindTableDTO dto);

	/**
	 * 필드 정보 리스트
	 * @param dto
	 * @return List<FieldVO>
	 */
	List<FieldVO> findTableFieldVOList(FindTableDTO dto);

	/**
	 * 인덱스 정보 리스트
	 * @param dto
	 * @return List<IndexVO>
	 */
	List<IndexVO> findTableIndexVOList(FindTableDTO dto);

	/**
	 * View 정보 리스트
	 * @param dto
	 * @return List<ViewVO>
	 */
	List<ViewVO> findViewVOList(FindTableDTO dto);

	/**
	 * database view detail search
	 * @param dto
	 * @return List<Map<String,String>>
	 */
	List<Map<String,String>> findViewDetailList(FindTableDTO dto);

	/**
	 * database view source search
	 * @param dto
	 * @return List<ViewVO>
	 */
	List<ViewVO> findViewVOSourceList(FindTableDTO dto);

	/**
	 * Procedure 정보 리스트
	 * @param dto
	 * @return List<ProcedureVO>
	 */
	List<ProcedureVO> findProcedureVOList(FindTableDTO dto);

	/**
	 * Procedure 상세 정보 리스트
	 * @param dto
	 * @return List<Map<String,String>>
	 */
	List<Map<String,String>> findProcedureDetailList(FindTableDTO dto);

	/**
	 * database stored procedure source search
	 * @param dto
	 * @return List<ProcedureVO>
	 */
	List<ProcedureVO> findProcedureVOSourceList(FindTableDTO dto);

	/**
	 * Function 정보 리스트
	 * @param dto
	 * @return List<FunctionVO>
	 */
	List<FunctionVO> findFunctionVOList(FindTableDTO dto);

	/**
	 * Function 상세 정보 리스트
	 * @param dto
	 * @return List<Map<String,String>>
	 */
	List<Map<String,String>> findFunctionDetailList(FindTableDTO dto);

	/**
	 * Function Source 정보 리스트
	 * @param dto
	 * @return List<FunctionVO>
	 */
	List<FunctionVO> findFunctionVOSourceList(FindTableDTO dto);

	/**
	 * Trigger 정보 리스트
	 * @param dto
	 * @return List<TriggerVO>
	 */
	List<TriggerVO> findTriggerVOList(FindTableDTO dto);

	/**
	 * Trigger 상세 정보 리스트
	 * @param dto
	 * @return List<Map<String,String>>
	 */
	List<Map<String,String>> findTriggerDetailList(FindTableDTO dto);

	/**
	 * Trigger Source 정보 리스트
	 * @param dto
	 * @return List<TriggerVO>
	 */
	List<TriggerVO> findTriggerVOSourceList(FindTableDTO dto);

	/**
	 * Sequence 리스트 조회
	 * @param dto
	 * @return List<SequenceVO>
	 */
	List<SequenceVO> findSequenceVOList(FindTableDTO dto);

	/**
	 * database Sequence Detail search
	 * @param dto
	 * @return List<Map<String,String>>
	 */
	List<Map<String,String>> findSequenceDetailList(FindTableDTO dto);


	/**
	 * database create table search
	 * @param serverInfo
	 * @return List<DatabaseDdlVO>
	 */
	List<DatabaseDdlVO> findShowCreateTable(FindTableDTO dto);

	/**
	 * Query Result Set List
	 * @param dto
	 * @return List<Map<String,String>>
	 */
	List<Map<String,String>> executeResultList(ExecuteResultListDTO dto);

	/**
	 * 자동완성을 위해 테이블의 모든 필드 리스트와 comment 를 조회 한다.
	 * @param dto
	 * @return List<FieldVO>
	 */
	List<FieldVO> findAllFieldList(FindServerInfoDTO dto);

	/**
	 * 실행 중인 쿼리를 중단한다.
	 * @param dto
	 */
	void killExecutedQuery(ExecuteResultListDTO dto);

	/**
	 * 등록되어 있는 서버 관련 캐시를 모두 삭제 한다
	 */
	void clearCache();
}