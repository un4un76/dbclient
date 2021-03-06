package com.song7749.dl.dbclient.service;

import static com.song7749.dl.dbclient.convert.ServerInfoConvert.convert;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.song7749.dl.dbclient.dto.DeleteServerInfoDTO;
import com.song7749.dl.dbclient.dto.ExecuteResultListDTO;
import com.song7749.dl.dbclient.dto.FindServerInfoDTO;
import com.song7749.dl.dbclient.dto.FindServerInfoListDTO;
import com.song7749.dl.dbclient.dto.FindTableDTO;
import com.song7749.dl.dbclient.dto.ModifyServerInfoDTO;
import com.song7749.dl.dbclient.dto.SaveServerInfoDTO;
import com.song7749.dl.dbclient.entities.ServerInfo;
import com.song7749.dl.dbclient.repositories.ServerInfoRepository;
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
import com.song7749.dl.member.repositories.MemberRepository;
import com.song7749.util.validate.ValidateGroupSelect;
import com.song7749.util.validate.annotation.Validate;


@Service("serverInfoManager")
@TransactionConfiguration(transactionManager = "dbClientTransactionManager", defaultRollback = true)
public class ServerInfoManagerImpl implements ServerInfoManager {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ServerInfoRepository serverInfoRepository;

	@Resource
	private DBclientDataSourceManager dbClientDataSourceManager;

	@Resource
	private MemberRepository memberRepository;

	@Override
	@Validate
	@Transactional("dbClientTransactionManager")
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void saveServerInfo(SaveServerInfoDTO dto) {

		ServerInfo serverInfo = new ServerInfo(dto.getHost(), dto.getHostAlias(),
				dto.getSchemaName(), dto.getAccount(), dto.getPassword(),
				dto.getDriver(), dto.getCharset(), dto.getPort());

		serverInfoRepository.save(serverInfo);
	}

	@Override
	@Validate
	@Transactional("dbClientTransactionManager")
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void modifyServerInfo(ModifyServerInfoDTO dto) {
		ServerInfo serverInfo = serverInfoRepository.find(new ServerInfo(dto
				.getServerInfoSeq()));
		if (null != serverInfo) {
			serverInfo.setHost(dto.getHost());
			serverInfo.setHostAlias(dto.getHostAlias());
			serverInfo.setSchemaName(dto.getSchemaName());
			serverInfo.setAccount(dto.getAccount());
			serverInfo.setPassword(dto.getPassword());
			serverInfo.setDriver(dto.getDriver());
			serverInfo.setCharset(dto.getCharset());
			serverInfo.setPort(dto.getPort());;
			serverInfoRepository.update(serverInfo);
		}
	}

	@Override
	@Transactional(value = "dbClientTransactionManager")
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void deleteServerInfo(DeleteServerInfoDTO dto) {
		// 서버 정보를 삭제
		serverInfoRepository.delete(new ServerInfo(dto.getServerInfoSeq()));
		// 회원 정보와 연결되어 있는 Database 접근 권한을 삭제
		memberRepository.removeMemberDatabases(dto.getServerInfoSeq());
	}

	@Override
	@Validate
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public ServerInfoVO findServerInfo(FindServerInfoDTO dto) {
		ServerInfo serverInfo = new ServerInfo(dto.getServerInfoSeq());
		return convert(serverInfoRepository.find(serverInfo));
	}

	@Override
	@Validate
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<ServerInfoVO> findServerInfoList(FindServerInfoListDTO dto) {
		return convert(serverInfoRepository.findServerInfoList(dto));
	}

	@Override
	@Transactional(value = "dbClientTransactionManager")
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void saveServerInfoFacade(List<SaveServerInfoDTO> list) {
		for (SaveServerInfoDTO saveServerInfoDTO : list) {
			saveServerInfo(saveServerInfoDTO);
		}
	}

	@Override
	@Transactional(value = "dbClientTransactionManager")
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void modifyServerInfoFacade(List<ModifyServerInfoDTO> list) {
		for (ModifyServerInfoDTO modifyServerInfoDTO : list) {
			modifyServerInfo(modifyServerInfoDTO);
		}
	}

	@Override
	@Transactional(value = "dbClientTransactionManager")
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void deleteServerInfoFacade(List<DeleteServerInfoDTO> list) {
		for (DeleteServerInfoDTO deleteServerInfoDTO : list) {
			deleteServerInfo(deleteServerInfoDTO);
		}
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<TableVO> findTableVOList(FindTableDTO dto) {

		return dbClientDataSourceManager.selectTableVOList(serverInfoRepository
				.find(new ServerInfo(dto.getServerInfoSeq())));
	}

	@Override
	@Validate
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<FieldVO> findTableFieldVOList(FindTableDTO dto) {

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectTableFieldVOList(info);
	}

	@Override
	@Validate
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<IndexVO> findTableIndexVOList(FindTableDTO dto) {

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectTableIndexVOList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<ViewVO> findViewVOList(FindTableDTO dto) {

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectViewVOList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<Map<String,String>> findViewDetailList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectViewDetailList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<ViewVO> findViewVOSourceList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectViewVOSourceList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<ProcedureVO> findProcedureVOList(FindTableDTO dto) {

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectProcedureVOList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<Map<String,String>> findProcedureDetailList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectProcedureDetailList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<ProcedureVO> findProcedureVOSourceList(FindTableDTO dto) {

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());
		return dbClientDataSourceManager.selectProcedureVOSourceList(info);
	}



	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<FunctionVO> findFunctionVOList(FindTableDTO dto) {

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectFunctionVOList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<Map<String,String>> findFunctionDetailList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectFunctionDetailList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<FunctionVO> findFunctionVOSourceList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectFunctionVOSourceList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<TriggerVO> findTriggerVOList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectTriggerVOList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<Map<String,String>> findTriggerDetailList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectTriggerDetailList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<TriggerVO> findTriggerVOSourceList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectTriggerVOSourceList(info);
	}


	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<SequenceVO> findSequenceVOList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectSequenceVOList(info);
	}

	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<Map<String,String>> findSequenceDetailList(FindTableDTO dto){

		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectSequenceDetailList(info);
	}


	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<DatabaseDdlVO> findShowCreateTable(FindTableDTO dto){
		ServerInfo info=serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq()));
		info.setName(dto.getName());

		return dbClientDataSourceManager.selectShowCreateTable(info);
	}


	@Override
	@Validate(VG = { ValidateGroupSelect.class })
	@Transactional(value = "dbClientTransactionManager", readOnly = true)
	@Cacheable(cacheName="com.song7749.cache.serverInfo.cache",cacheableInteceptorName="cacheAbleInterceptorImpl")
	public List<FieldVO> findAllFieldList(FindServerInfoDTO dto) {
		return dbClientDataSourceManager.selectAllFieldList(serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq())));
	}


	@Override
	@Validate
	@Transactional(value = "dbClientTransactionManager")
	public List<Map<String, String>> executeResultList(ExecuteResultListDTO dto) {
		return dbClientDataSourceManager.executeQueryList(serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq())), dto);
	}

	@Override
	@TriggersRemove(cacheName="com.song7749.cache.serverInfo.cache",removeAll=true)
	public void clearCache() {}

	@Override
	@Transactional(value = "dbClientTransactionManager")
	public void killExecutedQuery(ExecuteResultListDTO dto) {
		dbClientDataSourceManager.killQuery(serverInfoRepository.find(new ServerInfo(dto.getServerInfoSeq())), dto);
	}
}
