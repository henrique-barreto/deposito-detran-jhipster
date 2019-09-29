import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPerfil, defaultValue } from 'app/shared/model/perfil.model';

export const ACTION_TYPES = {
  FETCH_PERFIL_LIST: 'perfil/FETCH_PERFIL_LIST',
  FETCH_PERFIL: 'perfil/FETCH_PERFIL',
  CREATE_PERFIL: 'perfil/CREATE_PERFIL',
  UPDATE_PERFIL: 'perfil/UPDATE_PERFIL',
  DELETE_PERFIL: 'perfil/DELETE_PERFIL',
  RESET: 'perfil/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPerfil>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PerfilState = Readonly<typeof initialState>;

// Reducer

export default (state: PerfilState = initialState, action): PerfilState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PERFIL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PERFIL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PERFIL):
    case REQUEST(ACTION_TYPES.UPDATE_PERFIL):
    case REQUEST(ACTION_TYPES.DELETE_PERFIL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PERFIL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PERFIL):
    case FAILURE(ACTION_TYPES.CREATE_PERFIL):
    case FAILURE(ACTION_TYPES.UPDATE_PERFIL):
    case FAILURE(ACTION_TYPES.DELETE_PERFIL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PERFIL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PERFIL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PERFIL):
    case SUCCESS(ACTION_TYPES.UPDATE_PERFIL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PERFIL):
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

const apiUrl = 'api/perfils';

// Actions

export const getEntities: ICrudGetAllAction<IPerfil> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PERFIL_LIST,
    payload: axios.get<IPerfil>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPerfil> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PERFIL,
    payload: axios.get<IPerfil>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPerfil> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PERFIL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPerfil> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PERFIL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPerfil> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PERFIL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
