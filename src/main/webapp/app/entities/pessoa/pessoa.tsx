import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './pessoa.reducer';
import { IPessoa } from 'app/shared/model/pessoa.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPessoaProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Pessoa extends React.Component<IPessoaProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { pessoaList, match } = this.props;
    return (
      <div>
        <h2 id="pessoa-heading">
          Pessoas
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Pessoa
          </Link>
        </h2>
        <div className="table-responsive">
          {pessoaList && pessoaList.length > 0 ? (
            <Table responsive aria-describedby="pessoa-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>Data</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {pessoaList.map((pessoa, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${pessoa.id}`} color="link" size="sm">
                        {pessoa.id}
                      </Button>
                    </td>
                    <td>{pessoa.nome}</td>
                    <td>
                      <TextFormat type="date" value={pessoa.data} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${pessoa.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${pessoa.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${pessoa.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Pessoas found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ pessoa }: IRootState) => ({
  pessoaList: pessoa.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Pessoa);
