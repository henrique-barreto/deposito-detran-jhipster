import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Perfil from './perfil';
import Deposito from './deposito';
import Pessoa from './pessoa';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/perfil`} component={Perfil} />
      <ErrorBoundaryRoute path={`${match.url}/deposito`} component={Deposito} />
      <ErrorBoundaryRoute path={`${match.url}/pessoa`} component={Pessoa} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
