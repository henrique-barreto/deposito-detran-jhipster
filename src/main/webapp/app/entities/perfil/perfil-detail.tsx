import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './perfil.reducer';
import { IPerfil } from 'app/shared/model/perfil.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPerfilDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PerfilDetail extends React.Component<IPerfilDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { perfilEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Perfil [<b>{perfilEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="nome">Nome</span>
            </dt>
            <dd>{perfilEntity.nome}</dd>
            <dt>
              <span id="ativo">Ativo</span>
            </dt>
            <dd>{perfilEntity.ativo ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/perfil" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/perfil/${perfilEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ perfil }: IRootState) => ({
  perfilEntity: perfil.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PerfilDetail);
