import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Perfil from './perfil';
import PerfilDetail from './perfil-detail';
import PerfilUpdate from './perfil-update';
import PerfilDeleteDialog from './perfil-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PerfilUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PerfilUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PerfilDetail} />
      <ErrorBoundaryRoute path={match.url} component={Perfil} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PerfilDeleteDialog} />
  </>
);

export default Routes;
