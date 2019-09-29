import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDeposito, defaultValue } from 'app/shared/model/deposito.model';

export const ACTION_TYPES = {
  FETCH_DEPOSITO_LIST: 'deposito/FETCH_DEPOSITO_LIST',
  FETCH_DEPOSITO: 'deposito/FETCH_DEPOSITO',
  CREATE_DEPOSITO: 'deposito/CREATE_DEPOSITO',
  UPDATE_DEPOSITO: 'deposito/UPDATE_DEPOSITO',
  DELETE_DEPOSITO: 'deposito/DELETE_DEPOSITO',
  RESET: 'deposito/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDeposito>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DepositoState = Readonly<typeof initialState>;

// Reducer

export default (state: DepositoState = initialState, action): DepositoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DEPOSITO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DEPOSITO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DEPOSITO):
    case REQUEST(ACTION_TYPES.UPDATE_DEPOSITO):
    case REQUEST(ACTION_TYPES.DELETE_DEPOSITO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DEPOSITO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DEPOSITO):
    case FAILURE(ACTION_TYPES.CREATE_DEPOSITO):
    case FAILURE(ACTION_TYPES.UPDATE_DEPOSITO):
    case FAILURE(ACTION_TYPES.DELETE_DEPOSITO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DEPOSITO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_DEPOSITO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DEPOSITO):
    case SUCCESS(ACTION_TYPES.UPDATE_DEPOSITO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DEPOSITO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/depositos';

// Actions

export const getEntities: ICrudGetAllAction<IDeposito> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DEPOSITO_LIST,
    payload: axios.get<IDeposito>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDeposito> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DEPOSITO,
    payload: axios.get<IDeposito>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDeposito> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DEPOSITO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDeposito> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DEPOSITO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDeposito> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DEPOSITO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
