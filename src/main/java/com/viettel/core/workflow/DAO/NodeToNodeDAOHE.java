/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.DAO;

import com.viettel.utils.Constants;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.base.DAO.GenericDAOHibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author ChucHV
 */
public class NodeToNodeDAOHE extends GenericDAOHibernate<NodeToNode, Long> {

    public NodeToNodeDAOHE() {
        super(NodeToNode.class);
    }

    /**
     * hoangnv28
     * @param previousNode
     * @param nextNode
     * @param type
     * @param actionName
     * @return 
     */
    public List<NodeToNode> getNodeToNode(Long previousNode, Long nextNode,
            Long type, String actionName) {
        StringBuilder hql = new StringBuilder(
                "SELECT n FROM NodeToNode n WHERE n.isActive = 1 ");
        List listParams = new ArrayList<>();
        if (previousNode != null) {
            hql.append(" AND n.nodeToNodePK.previousId = ? ");
            listParams.add(previousNode);
        }
        if (nextNode != null) {
            hql.append(" AND n.nodeToNodePK.nextId = ? ");
            listParams.add(nextNode);
        }
        if (type != null) {
            hql.append(" AND n.type = ? ");
            listParams.add(type);
        }
        if (actionName != null) {
            /*
             * actionName là "Gửi xử lý" thì n.action có thể là "Gửi xử lý" hoặc
             * n.actionl là NULL. 
             */
            if (Constants.ACTION.NAME.DEFAULT.toLowerCase().equals(
                    actionName.toLowerCase())) {
                hql.append(" AND (lower(n.action) LIKE ? ESCAPE '/' OR n.action IS NULL )");
                listParams.add(actionName.toLowerCase());
            } else {
                hql.append(" AND lower(n.action) LIKE ? ESCAPE '/' ");
                listParams.add(actionName.toLowerCase());
            }
        }

        Query query = session.createQuery(hql.toString());
        for (int i = 0; i < listParams.size(); i++) {
            query.setParameter(i, listParams.get(i));
        }
        return query.list();
    }

    /**
     * hoangnv28
     * @param nodeId
     * @param actionType
     * @return 
     */
    public List getNextAction(Long nodeId, Long actionType) {
        if (nodeId == null) {
            return null;
        }
        StringBuilder hqlBuilder = new StringBuilder(
                "SELECT ntn FROM NodeToNode ntn WHERE ntn.isActive = 1 and ntn.previousId = ? ");
        List listParam = new ArrayList();
        listParam.add(nodeId);
        if (!Constants.NODE_ASSOCIATE_TYPE.ALL.equals(actionType)) {
            hqlBuilder.append(" AND ntn.type = ?");
            listParam.add(actionType);
        }
        Query query = getSession().createQuery(hqlBuilder.toString());
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i, listParam.get(i));
        }
        return query.list();
    }
    
    public List getNextAction(Long nodeId) {
        if (nodeId == null) {
            return null;
        }
        String hqlBuilder = "SELECT ntn FROM NodeToNode ntn WHERE "
                                 + " ntn.isActive = 1 and ntn.previousId = ? ";
        List listParam = new ArrayList();
        listParam.add(nodeId);
        
        Query query = getSession().createQuery(hqlBuilder);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i, listParam.get(i));
        }
        return query.list();
    }

    /**
     * hoangnv28
     * @param nodeId
     * @param actionType
     * @return 
     */
    public List getPreviousAction(Long nodeId, Long actionType) {
        StringBuilder hqlBuilder = new StringBuilder(
                "SELECT ntn FROM NodeToNode ntn WHERE ntn.isActive = 1 and ntn.nextId = ? ");
        List listParam = new ArrayList();
        listParam.add(nodeId);
        if (!Constants.NODE_ASSOCIATE_TYPE.ALL.equals(actionType)) {
            hqlBuilder.append(" AND ntn.type = ?");
            listParam.add(actionType);
        }
        Query query = getSession().createQuery(hqlBuilder.toString());
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i, listParam.get(i));
        }
        return query.list();
    }

    /* hoangnv28
     Lấy danh sách action từ 1 node trong luồng
     */
    public List<String> getActions(Long nodeId, Long deptId) {
        FlowDAOHE flowDAOHE = new FlowDAOHE();
        List<NodeToNode> listActions = null;
        List<String> result = new ArrayList<>();
        if (nodeId == null) {
            /*
             Lúc tạo văn bản đơn vị chưa có luồng (nodeId = null), 
             sau khi tạo xong, tìm luồng đơn vị
             */
            List listDeptFlow = flowDAOHE.getDeptFlow(deptId,
                    Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null);
            if (listDeptFlow != null) {
                Long firstNodeId = (Long) listDeptFlow.get(1);
                listActions = getNextAction(firstNodeId,
                        Constants.NODE_ASSOCIATE_TYPE.NORMAL);
            }
        } else {
            listActions = getNextAction(nodeId,
                    Constants.NODE_ASSOCIATE_TYPE.NORMAL);
        }

        //Những action trùng tên thì chỉ lấy 1 action mà thôi.
        if (listActions != null && !listActions.isEmpty()) {
            //NodeToNode action name luôn khác null
            for (NodeToNode ntn : listActions) {
                if (result.contains(ntn.getAction())) {
                    continue;
                }
                result.add(ntn.getAction());
            }
        }
        return result;
    }
    
    public List<NodeToNode> getNodeToNodes(Long previousId, Long nextId){
        
        StringBuilder hql = new StringBuilder(
                "SELECT n FROM NodeToNode n WHERE n.isActive = 1 ");
        hql.append(" AND n.previousId = ? ");
        hql.append(" AND n.nextId = ? ");

        Query query = session.createQuery(hql.toString());
        query.setParameter(0, previousId);
        query.setParameter(1, nextId);
        return query.list();
    }
    
    public NodeToNode getActionById(Long nodeToNodeId){
        StringBuilder hql = new StringBuilder(
                "SELECT n FROM NodeToNode n WHERE n.isActive = 1 ");
        hql.append(" AND n.id = ? ");

        Query query = session.createQuery(hql.toString());
        query.setParameter(0, nodeToNodeId);
        List<NodeToNode> rs = query.list();
        if(rs.isEmpty())
            return null;
        else
            return  rs.get(0);
    }
    
    public NodeToNode getNextNodeByNextId(Long nextId){
    	StringBuilder hql = new StringBuilder(
                "SELECT n FROM NodeToNode n WHERE n.isActive = 1 ");
        hql.append(" AND n.nextId = ? ");

        Query query = session.createQuery(hql.toString());
        query.setParameter(0, nextId);
        List<NodeToNode> rs = query.list();
        if(rs.isEmpty())
            return null;
        else
            return  rs.get(0);
    }
}
